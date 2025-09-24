package cc.lapiz.solstice.core.ui.action

import cc.lapiz.solstice.core.input.*
import cc.lapiz.solstice.core.ui.*
import org.joml.*

class ActionHandler {
	private val handlers = mutableMapOf<Action, MutableList<(UIElement) -> Unit>>()

	fun <T: Action> addHandler(action: T, handler: (UIElement) -> Unit) {
		handlers.computeIfAbsent(action) { mutableListOf() }.add(handler)
	}

	fun <T: Action> trigger(action: T, element: UIElement) {
		handlers[action]?.forEach {
			it(element)
		}
	}
}

fun UIElement.onAction(action: Action, handler: (UIElement) -> Unit) = apply {
	actonHandlers.addHandler(action, handler)
}

fun UIElement.onClick(handler: (UIElement) -> Unit) = apply {
	onAction(Action.Click, handler)
}

fun UIElement.onMousePress(handler: (UIElement) -> Unit) = apply {
	onAction(Action.MousePress, handler)
}

fun UIElement.onMouseRelease(handler: (UIElement) -> Unit) = apply {
	onAction(Action.MouseRelease, handler)
}

fun UIElement.onHover(handler: (UIElement,Boolean) -> Unit) = apply {
	onAction(Action.HoverEnter) { handler(it,true) }
	onAction(Action.HoverExit) { handler(it,false) }
}

fun UIElement.onMouseMove(handler: (UIElement,Float, Float) -> Unit) = apply {
	onAction(Action.MouseMove) {
		val (x, y) = Input.getMousePosition()
		handler(it,x,y)
	}
}