package cc.lapiz.solstice.core.event

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