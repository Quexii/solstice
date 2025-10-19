package cc.lapiz.solstice.core.assets

import cc.lapiz.solstice.core.assets.meta.Meta
import cc.lapiz.solstice.core.assets.meta.MetaGen
import cc.lapiz.solstice.core.assets.meta.MetaTexture
import cc.lapiz.solstice.core.assets.types.Sprite
import cc.lapiz.solstice.core.utils.logger
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.*

object Assets {
    private val assetsDir = File("assets")
    private val LOGGER = logger(this::class.java)
    private var running = false
    private var watcherThread: Thread? = null

    private val toLoad = mutableListOf<Asset>()
    private val assets = mutableListOf<Asset>()

    fun init() {
        assetsDir.mkdir()
        running = true
        watcherThread = Thread { initWatcher() }
        watcherThread?.isDaemon = true
        watcherThread?.start()
    }

    fun loadTick() {
        toLoad.forEach {
            it.load()
            assets.add(it)
        }
        toLoad.clear()
    }

    fun cleanup() {
        running = false
        watcherThread?.interrupt()
    }

    private fun initWatcher() {
        val watcher = FileSystems.getDefault().newWatchService()
        val files = mutableListOf<Path>()
        val filesToRemove = mutableListOf<Path>()

        val walked = Files.walk(assetsDir.absoluteFile.toPath()).toList()
        walked.filter { !Files.isDirectory(it) }.forEach {
            files.add(it)
        }

        walked.filter { Files.isDirectory(it) }.forEach {
            it.register(
                watcher,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY
            )
        }

        LOGGER.info("Watching assets dir at: ${assetsDir.absoluteFile.path}")

        for (file in files.filter { it -> !it.fileName.toString().endsWith(".asset") }) {
            if (files.any { it.fileName.toString() == ".${file.fileName}.asset" }) {
                MetaGen.modify(file)
            } else MetaGen.generate(file)
        }

        while (running) {
            val key = try {
                watcher.take()
            } catch (e: InterruptedException) {
                break
            } catch (e: ClosedWatchServiceException) {
                break
            }

            for (event in key.pollEvents()) {
                val kind = event.kind()
                val dir = key.watchable() as Path
                val file = dir.resolve(event.context() as Path).toAbsolutePath()

                when (kind) {
                    StandardWatchEventKinds.ENTRY_CREATE -> {
                        if (!file.fileName.toString().endsWith(".asset"))
                            MetaGen.generate(file)
                        else
                            files.add(file)
                    }

                    StandardWatchEventKinds.ENTRY_DELETE -> {
                        if (!file.fileName.toString().endsWith(".asset"))
                            MetaGen.delete(file)
                    }

                    StandardWatchEventKinds.ENTRY_MODIFY -> {
                        if (!file.fileName.toString().endsWith(".asset"))
                            MetaGen.modify(file)
                    }
                }
            }

            files.forEach {
                if (it.fileName.toString().endsWith(".asset")) {
                    addAsset(it)
                }
                filesToRemove.add(it)
            }

            filesToRemove.forEach { files.remove(it) }

            if (!key.reset()) break
        }

        watcher.close()
    }

    private fun addAsset(path: Path) {
        val gen = MetaGen.fromCachedPath(path)
        when (gen) {
            is MetaTexture -> toLoad.add(Sprite(gen.content!!))
        }
    }

    fun getAll(): List<Asset> = assets.toList()
    inline fun <reified T: Asset> getType(): List<T> = getAll().filterIsInstance<T>().toList()
    inline fun <reified T: Asset> getTypeByUID(uid: String): T = getType<T>().first { it.meta.uid == uid }
    inline fun <reified T: Meta.BaseSerialized> updateAssetMetadata(meta: Meta<*>, newData: T) {
        MetaGen.update(meta.path, Json.encodeToString(newData), true)
    }
}