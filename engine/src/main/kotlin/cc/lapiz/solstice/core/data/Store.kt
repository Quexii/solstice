package cc.lapiz.solstice.data

data class Store(val value: Any?) {
	private var _internalValue: Any? = value

	fun set(newValue: Any?) {
		_internalValue = newValue
	}

	fun get(): Any? = _internalValue
}