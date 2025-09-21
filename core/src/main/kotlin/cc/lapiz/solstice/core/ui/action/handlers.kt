package cc.lapiz.solstice.core.ui.action

import cc.lapiz.solstice.core.ui.UIElement

class ActionHandler {
	private val handlers = mutableMapOf<Action, MutableList<() -> Unit>>()

	fun addHandler(action: Action, handler: () -> Unit) {
		handlers.computeIfAbsent(action) { mutableListOf() }.add(handler)
	}

	fun removeHandler(action: Action, handler: () -> Unit) {
		handlers[action]?.remove(handler)
	}

	fun trigger(action: Action) {
		handlers[action]?.forEach { it() }
	}
}

fun UIElement.onClick(handler: () -> Unit) = apply {
	actonHandlers.addHandler(Action.Click, handler)
}

fun UIElement.onHover(handler: (Boolean) -> Unit) = apply {
	actonHandlers.addHandler(Action.HoverEnter) { handler(true) }
	actonHandlers.addHandler(Action.HoverExit) { handler(false) }
}