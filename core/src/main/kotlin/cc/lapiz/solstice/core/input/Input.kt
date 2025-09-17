package cc.lapiz.solstice.core.input

import cc.lapiz.solstice.core.input.InputHandler
import org.joml.Vector2f

object Input {
	private var handler: InputHandler? = null

	fun setHandler(handler: InputHandler) {
		this.handler = handler
	}

	fun isKeyPressed(key: Key): Boolean {
		return handler?.isKeyPressed(key) ?: false
	}

	fun isMouseButtonPressed(button: MouseButton): Boolean {
		return handler?.isMouseButtonPressed(button) ?: false
	}

	fun getMousePosition(): Vector2f {
		return handler?.getMousePosition() ?: Vector2f()
	}
}