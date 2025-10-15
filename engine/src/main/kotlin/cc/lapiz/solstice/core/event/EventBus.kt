package cc.lapiz.solstice.event

class EventBus {
	private val listeners = mutableMapOf<Class<out Event>, MutableList<(Event) -> Unit>>()

	fun <T : Event> register(eventType: Class<T>, listener: (T) -> Unit) {
		val eventListeners = listeners.getOrPut(eventType) { mutableListOf() }
		@Suppress("UNCHECKED_CAST")
		eventListeners.add(listener as (Event) -> Unit)
	}

	fun unregister(eventType: Class<out Event>, listener: (Event) -> Unit) {
		listeners[eventType]?.remove(listener)
	}

	fun post(event: Event) {
		listeners[event::class.java]?.forEach { it(event) }
	}
}