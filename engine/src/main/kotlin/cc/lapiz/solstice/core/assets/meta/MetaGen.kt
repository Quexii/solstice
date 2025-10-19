package cc.lapiz.solstice.core.assets.meta

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension

object MetaGen {
    private val associateMap = mutableMapOf<Path, Meta<*>>()
    private val metaFactories = mutableMapOf<String, (Path) -> Meta<*>>()
    private val extensionToType = mutableMapOf<String, String>()
    private val assetsRoot = Path.of("assets")
    private val virtualRoot = assetsRoot.resolve(".virtual")
    private val parser = Json { ignoreUnknownKeys = true }

    init {
        registerMetaType("texture") { MetaTexture(it) }
        registerMetaType("shader") { MetaShader(it) }
        registerMetaType("data") { MetaData(it) }
        registerMetaType("prefab") { MetaPrefab(it) }
        registerMetaType("unknown") { MetaUnknown(it) }

        registerExtension("png", "texture")
        registerExtension("jpg", "texture")
        registerExtension("jpeg", "texture")
        registerExtension("tga", "texture")
        registerExtension("bmp", "texture")

        registerExtension("shader", "shader")
        registerExtension("frag", "shader")
        registerExtension("vert", "shader")
        registerExtension("glsl", "shader")

        registerExtension("json", "data")
        registerExtension("yaml", "data")
        registerExtension("toml", "data")
        registerExtension("prefab", "prefab")
    }

    fun determineType(file: Path): Meta<*> {
        val ext = file.extension.lowercase()
        extensionToType[ext]?.let { return instantiate(it, file) }

        val mime = Files.probeContentType(file)
        return when {
            mime?.startsWith("image/") == true -> instantiate("texture", file)
            mime?.contains("json") == true -> instantiate("data", file)
            else -> instantiate("unknown", file)
        }
    }

    fun generate(path: Path) {
        val gen = determineType(path)
        val file = jsonFile(path)
        cacheMeta(file.toPath(), gen)
        file.writeText(gen.onCreate())
    }

    fun modify(path: Path) {
        val file = jsonFile(path)
        val original = file.readText()
        update(path, original, false)
    }

    fun update(path: Path, content: String, force: Boolean) {
        val file = jsonFile(path)
        val gen = associateMap[file.toPath().normalize()] ?: determineType(path).also {
            cacheMeta(file.toPath(), it)
        }
        update(gen, content, force)
    }

    fun update(meta: Meta<*>, content: String, force: Boolean) {
        val file = jsonFile(meta.path)
        cacheMeta(file.toPath(), meta)
        file.writeText(meta.onModify(content, force))
    }

    fun delete(path: Path) {
        val file = jsonFile(path)
        associateMap.remove(file.toPath().normalize())
        if (file.exists()) file.delete()
    }

    fun fromCachedPath(path: Path): Meta<*> = associateMap[path.normalize()] ?: loadFromAsset(path)

    fun jsonFile(path: Path): File {
        val metaFileName = ".${path.fileName}.asset"
        val metaPath = path.resolveSibling(metaFileName).normalize()
        return metaPath.toFile()
    }

    fun registerMetaType(type: String, factory: (Path) -> Meta<*>) {
        metaFactories[type] = factory
    }

    fun registerExtension(extension: String, type: String) {
        extensionToType[extension.lowercase()] = type
    }

    fun registerVirtual(relativeName: String, builder: (Path) -> Meta<*>): Path {
        val relative = Path.of(relativeName)
        require(!relative.isAbsolute) { "Virtual asset name must be relative" }

        Files.createDirectories(virtualRoot)
        val sourcePath = virtualRoot.resolve(relative).normalize()
        Files.createDirectories(sourcePath.parent)

        val meta = builder(sourcePath)
        require(meta.path.normalize() == sourcePath) {
            "Virtual meta path mismatch: expected $sourcePath but was ${meta.path}"
        }

        val jsonFile = jsonFile(sourcePath)
        val jsonPath = jsonFile.toPath().normalize()
        cacheMeta(jsonPath, meta)
        jsonFile.writeText(meta.onCreate())
        return jsonPath
    }

    private fun cacheMeta(assetPath: Path, meta: Meta<*>) {
        associateMap[assetPath.normalize()] = meta
    }

    private fun instantiate(type: String, path: Path): Meta<*> {
        val factory = metaFactories[type] ?: error("No meta factory registered for type: $type")
        return factory(path)
    }

    private fun loadFromAsset(assetPath: Path): Meta<*> {
        val normalized = assetPath.normalize()
        val text = runCatching { Files.readString(normalized) }.getOrElse {
            throw IllegalStateException("Cannot read asset metadata at $assetPath", it)
        }

        if (text.isBlank()) {
            val meta = instantiate("unknown", assetPath)
            cacheMeta(normalized, meta)
            return meta
        }

        val jsonElement = runCatching { parser.parseToJsonElement(text).jsonObject }.getOrElse {
            val meta = instantiate("unknown", assetPath)
            cacheMeta(normalized, meta)
            return meta
        }

        val type = jsonElement["metaType"]?.jsonPrimitive?.content ?: "unknown"
        val metaPathString = jsonElement["path"]?.jsonPrimitive?.content
        val metaPath = metaPathString?.let { Path.of(it) } ?: assetPath
        val meta = instantiate(type, metaPath)
        meta.loadFromDisk(text)
        cacheMeta(normalized, meta)
        return meta
    }
}