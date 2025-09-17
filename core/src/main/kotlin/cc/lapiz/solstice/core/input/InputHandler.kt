package cc.lapiz.solstice.core.input

import org.joml.Vector2f

interface InputHandler {
	fun isKeyPressed(key: Key): Boolean
	fun isMouseButtonPressed(button: MouseButton): Boolean
	fun getMousePosition(): Vector2f
}