package cc.lapiz.solstice.core.game.components.meta

import kotlinx.serialization.json.JsonObject

interface JsonSerializable {
    fun toJson(): JsonObject
}