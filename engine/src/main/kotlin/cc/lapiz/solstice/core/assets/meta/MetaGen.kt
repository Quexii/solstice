package cc.lapiz.solstice.core.assets.meta

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension

object MetaGen {
    fun determineType(file: Path): Meta {
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

    }

    fun modify(path: Path) {

    }

    fun delete(path: Path) {
        val file = jsonFile(path)
    }

    private fun jsonFile(path: Path): File {
        val metaFileName = ".meta.${path.fileName}.json"
        val metaPath = path.resolveSibling(metaFileName).normalize()
        return metaPath.toFile()
    }
}