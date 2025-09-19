package cc.lapiz.solstice.core.ui.behavior

import cc.lapiz.solstice.core.event.Event

interface UIBehavior {
	val func: () -> Unit
	fun onEvent(event: Event)
}