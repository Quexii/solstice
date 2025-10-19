package cc.lapiz.solstice.core.assets.meta

import java.nio.file.Path

class MetaData(path: Path) : Meta<Nothing>(path) {
    override val version: String
        get() = "0.1"

    override fun onCreate(): String {
        return ""
    }

    override fun onModify(original: String, force: Boolean): String {
        return ""
    }
}