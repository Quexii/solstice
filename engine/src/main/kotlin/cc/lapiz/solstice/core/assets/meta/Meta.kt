package cc.lapiz.solstice.core.assets.meta

import java.nio.file.Path

abstract class Meta(val path: Path) {
    abstract val version: String
    abstract fun onCreate()
    abstract fun onModify()
}