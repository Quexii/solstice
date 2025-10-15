package cc.lapiz.solstice.options

import kotlinx.serialization.Serializable

@Serializable
data class OptionsData(
	val values: List<OptionValue> = emptyList()
)