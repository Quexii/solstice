package cc.lapiz.solstice.core.ui

import cc.lapiz.solstice.core.event.*
import cc.lapiz.solstice.core.ui.widget.Widget
import cc.lapiz.solstice.core.window.Window

class UI() {
	val root = Widget(0f, 0f, Window.width().toFloat(), Window.height().toFloat())

	fun draw() {
		root.draw()
	}

	fun onInput(event: InputEvent) {
		root.onInput(event)
	}

	fun build(init: Widget.() -> Unit) {
		root.init()
	}
}
