package cc.lapiz.solstice.core.options

import kotlinx.serialization.Serializable

@Serializable
data class OptionsData(
	val values: List<OptionValue> = emptyList()
)