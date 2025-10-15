package cc.lapiz.solstice.data

import kotlinx.serialization.Serializable

@Serializable
data class UVRect(var u0: Float = 0f, var v0: Float = 0f, var u1: Float = 0f, var v1: Float = 0f)