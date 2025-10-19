package cc.lapiz.solstice.core.options

import kotlinx.serialization.Serializable

@Serializable
data class OptionValue(
	val key: String,
	val value: String
)