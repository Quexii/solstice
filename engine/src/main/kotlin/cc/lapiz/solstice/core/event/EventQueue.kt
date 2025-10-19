package cc.lapiz.solstice.core.event

import cc.lapiz.solstice.core.event.Event
import java.util.concurrent.ConcurrentLinkedQueue

class EventQueue {
	private val eventQueue = ConcurrentLinkedQueue<Event>()

	fun push(event: Event) {
		eventQueue.add(event)
	}

	fun poll(): Event? {
		return eventQueue.poll()
	}
}