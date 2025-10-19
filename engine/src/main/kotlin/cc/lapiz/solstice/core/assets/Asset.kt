package cc.lapiz.solstice.core.assets

import cc.lapiz.solstice.core.assets.meta.Meta

abstract class Asset(val meta: Meta.BaseSerialized) {
    open fun load() {}
}