package cc.lapiz.solstice.core.options

class StringOption(
	key: String,
	defaultValue: String,
	description: String = "",
	val maxLength: Int = Int.MAX_VALUE
) : Option<String>(key, defaultValue, description) {
	override fun parse(value: String): String = value
	override fun validate(value: String): Boolean = value.length <= maxLength
}

class IntOption(
	key: String,
	defaultValue: Int,
	description: String = "",
	val min: Int = Int.MIN_VALUE,
	val max: Int = Int.MAX_VALUE
) : Option<Int>(key, defaultValue, description) {
	override fun parse(value: String): Int? = value.toIntOrNull()
	override fun validate(value: Int): Boolean = value in min..max
}

class BooleanOption(
	key: String,
	defaultValue: Boolean,
	description: String = ""
) : Option<Boolean>(key, defaultValue, description) {
	override fun parse(value: String): Boolean? = value.toBooleanStrictOrNull()
	override fun validate(value: Boolean): Boolean = true
}

class DoubleOption(
	key: String,
	defaultValue: Double,
	description: String = "",
	val min: Double = Double.NEGATIVE_INFINITY,
	val max: Double = Double.POSITIVE_INFINITY
) : Option<Double>(key, defaultValue, description) {
	override fun parse(value: String): Double? = value.toDoubleOrNull()
	override fun validate(value: Double): Boolean = value in min..max
}