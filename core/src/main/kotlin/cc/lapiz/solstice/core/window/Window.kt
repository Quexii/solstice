package cc.lapiz.solstice.core.window

import cc.lapiz.solstice.core.event.WindowEvent
import cc.lapiz.solstice.core.rendering.platform.Graphics
import org.joml.Vector2i

object Window {
	private var size = Vector2i()
	private var position = Vector2i()
	private var focused = true

	fun initialize(width: Int, height: Int) {
		size.set(width, height)
		focused = true
	}

	fun handleEvents(event: WindowEvent) {
		when (event) {
			is WindowEvent.Resize -> {
				size.set(event.width, event.height)
				Graphics.viewport(0, 0, event.width, event.height)
			}
			is WindowEvent.Move -> {
				position.set(event.x, event.y)
			}
			is WindowEvent.Focus -> {
				focused = event.focused
			}
		}
	}

	fun width() = size.x
	fun height() = size.y
	fun position() = position
	fun isFocused() = focused
}