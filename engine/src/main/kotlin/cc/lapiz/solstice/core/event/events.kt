package cc.lapiz.solstice.core.event

import cc.lapiz.solstice.core.input.Key
import cc.lapiz.solstice.core.input.MouseButton
import org.joml.Vector2f

sealed interface Event

sealed class InputEvent : Event {
	data class KeyPress(val key: Key) : InputEvent()
	data class KeyRelease(val key: Key) : InputEvent()
	data class KeyType(val char: Char) : InputEvent()
	data class MouseMove(val position: Vector2f) : InputEvent()
	data class MousePress(val button: MouseButton) : InputEvent()
	data class MouseRelease(val button: MouseButton) : InputEvent()
	data class MouseScroll(val amount: Float) : InputEvent()
}

sealed class WindowEvent : Event {
	data class Resize(val width: Int, val height: Int) : WindowEvent()
	data class Move(val x: Int, val y: Int) : WindowEvent()
	data class Focus(val focused: Boolean) : WindowEvent()
}

fun isMouseEvent(event: Event): Boolean {
	return event is InputEvent.MouseMove || event is InputEvent.MousePress || event is InputEvent.MouseRelease || event is InputEvent.MouseScroll
}

fun isKeyEvent(event: Event): Boolean {
	return event is InputEvent.KeyPress || event is InputEvent.KeyRelease || event is InputEvent.KeyType
}