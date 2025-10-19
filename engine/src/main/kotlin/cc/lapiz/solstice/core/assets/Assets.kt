package cc.lapiz.solstice.core.assets

import cc.lapiz.solstice.core.assets.meta.Meta
import cc.lapiz.solstice.core.assets.meta.MetaGen
import cc.lapiz.solstice.core.assets.meta.MetaPrefab
import cc.lapiz.solstice.core.assets.meta.MetaTexture
import cc.lapiz.solstice.core.assets.types.Sprite
import cc.lapiz.solstice.core.assets.types.PrefabAsset
import cc.lapiz.solstice.core.utils.logger
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.*
import java.util.ArrayDeque

object Assets {
    private val assetsDir = File("assets")
    private val LOGGER = logger(this::class.java)
    private var running = false
    private var watcherThread: Thread? = null

    private val loadQueue = ArrayDeque<Pair<Path, Asset>>()
    private val pendingAssets = mutableSetOf<Path>()
    private val assets = mutableMapOf<String, Asset>()
    private val assetsByPath = mutableMapOf<Path, String>()

    fun init() {
        assetsDir.mkdir()
        running = true
        watcherThread = Thread { initWatcher() }
        watcherThread?.isDaemon = true
        watcherThread?.start()
    }

    fun loadTick() {
        while (loadQueue.isNotEmpty()) {
            val (path, asset) = loadQueue.removeFirst()
            pendingAssets.remove(path)
            runCatching { asset.load() }
                .onFailure { LOGGER.error("Failed to load asset at $path", it) }
                .onSuccess {
                    assets[asset.meta.uid] = asset
                    assetsByPath[path] = asset.meta.uid
                    LOGGER.debug("Loaded asset ${asset.meta.uid} from $path")
                }
        }
    }

    fun cleanup() {
        running = false
        watcherThread?.interrupt()
    }

    private fun initWatcher() {
        val watcher = FileSystems.getDefault().newWatchService()
        val files = mutableSetOf<Path>()
        val filesToRemove = mutableSetOf<Path>()

        val walked = Files.walk(assetsDir.absoluteFile.toPath()).toList()
        walked.filter { !Files.isDirectory(it) }.forEach {
            files.add(it.normalize())
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
            val assetExists = files.any { it.fileName.toString() == ".${file.fileName}.asset" }
            if (assetExists) {
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
                val file = dir.resolve(event.context() as Path).toAbsolutePath().normalize()

                when (kind) {
                    StandardWatchEventKinds.ENTRY_CREATE -> {
                        if (!file.fileName.toString().endsWith(".asset")) {
                            MetaGen.generate(file)
                            files.add(file)
                        } else {
                            files.add(file)
                        }
                    }

                    StandardWatchEventKinds.ENTRY_DELETE -> {
                        if (!file.fileName.toString().endsWith(".asset")) {
                            MetaGen.delete(file)
                        } else {
                            removeAsset(file)
                        }
                    }

                    StandardWatchEventKinds.ENTRY_MODIFY -> {
                        if (!file.fileName.toString().endsWith(".asset")) {
                            MetaGen.modify(file)
                            files.add(file)
                        } else {
                            files.add(file)
                        }
                    }
                }
            }

            files.forEach {
                if (Files.notExists(it)) {
                    filesToRemove.add(it)
                    continue
                }

                if (it.fileName.toString().endsWith(".asset")) {
                    enqueueAsset(it)
                }
                filesToRemove.add(it)
            }

            files.removeAll(filesToRemove)
            filesToRemove.clear()

            if (!key.reset()) break
        }

        watcher.close()
    }

    private fun enqueueAsset(path: Path) {
        val normalized = path.normalize()
        if (!pendingAssets.add(normalized)) return

        val gen = runCatching { MetaGen.fromCachedPath(normalized) }
            .onFailure { LOGGER.error("Failed to resolve metadata for asset at $normalized", it) }
            .getOrNull() ?: run {
                pendingAssets.remove(normalized)
                return
            }

        when (gen) {
            is MetaTexture -> {
                val data = gen.content ?: run {
                    LOGGER.warn("Texture metadata for $normalized is missing content")
                    pendingAssets.remove(normalized)
                    return
                }
                loadQueue.add(normalized to Sprite(data))
            }

            is MetaPrefab -> {
                val data = gen.content ?: run {
                    LOGGER.warn("Prefab metadata for $normalized is missing content")
                    pendingAssets.remove(normalized)
                    return
                }
                loadQueue.add(normalized to PrefabAsset(data))
            }

            else -> {
                LOGGER.debug("No runtime loader registered for asset at $normalized")
                pendingAssets.remove(normalized)
            }
        }
    }

    private fun removeAsset(path: Path) {
        val normalized = path.normalize()
        pendingAssets.remove(normalized)
        val uid = assetsByPath.remove(normalized) ?: return
        assets.remove(uid)
        LOGGER.debug("Removed asset $uid from $normalized")
    }

    @PublishedApi
    internal val loadedAssets: Collection<Asset>
        get() = assets.values

    @PublishedApi
    internal fun assetFor(uid: String): Asset = assets[uid] ?: error("No asset with uid $uid")

    fun getAll(): List<Asset> = loadedAssets.toList()
    inline fun <reified T: Asset> getType(): List<T> = loadedAssets.filterIsInstance<T>().toList()
    inline fun <reified T: Asset> getTypeByUID(uid: String): T = assetFor(uid) as? T
        ?: error("No asset with uid $uid")
    inline fun <reified T: Meta.BaseSerialized> updateAssetMetadata(meta: Meta<*>, newData: T) {
        MetaGen.update(meta, Json.encodeToString(newData), true)
    }

    fun registerVirtualAsset(relativeName: String, builder: (Path) -> Meta<*>): Path {
        val metaPath = MetaGen.registerVirtual(relativeName, builder)
        enqueueAsset(metaPath)
        return metaPath
    }
}