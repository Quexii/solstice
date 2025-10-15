package cc.lapiz.solstice.core.assets

import cc.lapiz.solstice.core.assets.meta.MetaGen
import cc.lapiz.solstice.utils.logger
import java.io.File
import java.nio.file.*
import kotlin.io.path.absolutePathString
import kotlin.io.path.appendText

object Assets {
    private val assetsDir = File("assets")
    private val LOGGER = logger(this::class.java)
    private var running = false

    fun init() {
        assetsDir.mkdir()
        running = true
        Thread { initWatcher() }.start()
    }

    fun cleanup() {
        running = false
    }

    private fun initWatcher() {
        val watcher = FileSystems.getDefault().newWatchService()
        Files.walk(assetsDir.absoluteFile.toPath()).filter { Files.isDirectory(it) }.forEach {
            it.register(
                watcher,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY
            )
        }

        LOGGER.info("Watching assets dir at: ${assetsDir.absoluteFile.path}")

        while (running) {
            val key = watcher.take()

            for (event in key.pollEvents()) {
                val kind = event.kind()
                val dir = key.watchable() as Path
                val file = dir.resolve(event.context() as Path).toAbsolutePath()
                if (file.fileName.toString().startsWith(".meta.")) continue

                when (kind) {
                    StandardWatchEventKinds.ENTRY_CREATE -> MetaGen.generate(file)
                    StandardWatchEventKinds.ENTRY_DELETE -> MetaGen.delete(file)
                    StandardWatchEventKinds.ENTRY_MODIFY -> MetaGen.modify(file)
                }
            }

            if (!key.reset()) break
        }
    }

    private fun genMetadata(path: Path) {

        if (!jsonFile.exists()) {
            jsonFile.createNewFile()
            println("Created metadata: $jsonFile")
        }
    }


    private fun modMetadata(path: Path) {
    }

    private fun delMetadata(path: Path) {
        val metaFileName = ".meta.${path.fileName}.json"
        val metaPath = path.resolveSibling(metaFileName).normalize()
        val jsonFile = metaPath.toFile()

        if (jsonFile.exists()) {
            jsonFile.delete()
        }
    }
}