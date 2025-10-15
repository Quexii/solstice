package cc.lapiz.solstice.options

abstract class Option<T>(
	val key: String,
	val defaultValue: T,
	val description: String = ""
) {
	abstract fun parse(value: String): T?
	abstract fun validate(value: T): Boolean
}