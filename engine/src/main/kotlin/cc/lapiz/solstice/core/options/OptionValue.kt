package cc.lapiz.solstice.options

import kotlinx.serialization.Serializable

@Serializable
data class OptionValue(
	val key: String,
	val value: String
)