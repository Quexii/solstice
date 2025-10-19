package cc.lapiz.solstice.core.assets.meta

import kotlinx.serialization.Serializable
import java.nio.file.Path

abstract class Meta<SerializeType>(val path: Path) {
    abstract val version: String
    abstract val metaType: String
    var content: SerializeType? = null
    abstract fun onCreate(): String
    abstract fun onModify(original: String, force: Boolean): String
    open fun loadFromDisk(serialized: String) {}

    @Serializable
    open class BaseSerialized(
        var version: String = "",
        var uid: String = "",
        var path: String = "",
        var metaType: String = "",
    )
}