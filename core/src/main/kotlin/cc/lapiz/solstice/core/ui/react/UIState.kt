package cc.lapiz.solstice.core.ui.react

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class UIState {
	val stateMap = mutableMapOf<String, Any?>()
	val observerMap = mutableMapOf<String, MutableList<(Any?) -> Unit>>()

	inline fun <reified T> remember(key: String, initialValue: T): ReadWriteProperty<Any?, T> {
		if (!stateMap.containsKey(key)) {
			stateMap[key] = initialValue
		}

		return object : ReadWriteProperty<Any?, T> {
			override fun getValue(thisRef: Any?, property: KProperty<*>): T {
				@Suppress("UNCHECKED_CAST")
				return stateMap[key] as T
			}

			override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
				if (stateMap[key] != value) {
					stateMap[key] = value
					observerMap[key]?.forEach { observer ->
						observer(value)
					}
				}
			}
		}
	}

	inline fun <reified T> rememberObservable(key: String, initialValue: T, noinline onChange: (T) -> Unit = {}): ReadWriteProperty<Any?, T> {
		if (!stateMap.containsKey(key)) {
			stateMap[key] = initialValue
			observerMap[key] = mutableListOf()
		}

		observerMap[key]?.add { newValue ->
			@Suppress("UNCHECKED_CAST")
			onChange(newValue as T)
		}

		return remember(key, initialValue)
	}

	fun <T> observe(key: String, observer: (T) -> Unit) {
		observerMap.getOrPut(key) { mutableListOf() }.add { value ->
			@Suppress("UNCHECKED_CAST")
			observer(value as T)
		}
	}

	fun clearState() {
		stateMap.clear()
		observerMap.clear()
	}
}