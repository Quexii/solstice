package cc.lapiz.solstice.core.game

import cc.lapiz.solstice.core.event.*
import cc.lapiz.solstice.core.game.ecs.*
import cc.lapiz.solstice.core.ui.widget.Widget
import cc.lapiz.solstice.core.window.Window

abstract class Scene {
	val esc = ECS()

	abstract val ui: Widget

	open fun onEnter() {
		resize(Window.width(), Window.height())
	}

	open fun onExit() {
		esc.clear()
	}
	open fun update(delta: Float) {}
	open fun render() {}
	open fun onEvent(evnet: Event) {
		ui.onEvent(evnet)
	}
	open fun resize(width: Int, height: Int) {
		ui.resize(width.toFloat(), height.toFloat())
	}
	open fun nanovg() {
		ui.draw()
	}
}