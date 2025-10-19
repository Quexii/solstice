package cc.lapiz.solstice.core.assets.meta

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.nio.file.Path

abstract class Meta<SerializeType>(val path: Path) {
    abstract val version: String
    var content: SerializeType? = null
    abstract fun onCreate(): String
    abstract fun onModify(original: String, force: Boolean): String

    @Serializable
    open class BaseSerialized(
        var version: String = "",
        var uid: String = "",
        var path: String = ""
    )
}