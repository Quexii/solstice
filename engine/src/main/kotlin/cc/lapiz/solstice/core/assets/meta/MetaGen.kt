package cc.lapiz.solstice.core.assets.meta

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension

object MetaGen {
    private val associateMap = mutableMapOf<Path, Meta<*>>()

    fun determineType(file: Path): Meta<*> {
        val ext = file.extension.lowercase()
        val meta = when (ext) {
            "png", "jpg", "jpeg", "tga", "bmp" -> MetaTexture(file)
            "shader", "frag", "vert", "glsl" -> MetaShader(file)
            "json", "yaml", "toml" -> MetaData(file)
            else -> {
                val mime = Files.probeContentType(file)
                when {
                    mime?.startsWith("image/") == true -> MetaTexture(file)
                    mime?.contains("json") == true -> MetaData(file)
                    else -> MetaUnknown(file)
                }
            }
        }

        return meta
    }

    fun generate(path: Path) {
        val gen = determineType(path)
        val file = jsonFile(path)
        associateMap[file.toPath()] = gen
        file.writeText(gen.onCreate())
    }

    fun modify(path: Path) {
        val file = jsonFile(path)
        val original = file.readText()
        update(path, original, false)
    }

    fun update(path: Path, content: String, force: Boolean) {
        val gen = determineType(path)
        val file = jsonFile(path)
        associateMap[file.toPath()] = gen
        file.writeText(gen.onModify(content, force))
    }

    fun delete(path: Path) {
        val file = jsonFile(path)
        if (file.exists()) file.delete()
    }

    fun fromCachedPath(path: Path): Meta<*> = associateMap[path] ?: error("No gen found for path: $path")

    fun jsonFile(path: Path): File {
        val metaFileName = ".${path.fileName}.asset"
        val metaPath = path.resolveSibling(metaFileName).normalize()
        return metaPath.toFile()
    }
}