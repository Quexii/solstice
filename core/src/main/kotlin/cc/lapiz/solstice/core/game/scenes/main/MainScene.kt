package cc.lapiz.solstice.core.game.scenes.main

import cc.lapiz.solstice.core.game.*
import cc.lapiz.solstice.core.input.Input
import cc.lapiz.solstice.core.rendering.nanovg.*
import cc.lapiz.solstice.core.ui.*
import cc.lapiz.solstice.core.ui.action.onMouseMove
import cc.lapiz.solstice.core.ui.action.onMousePress
import cc.lapiz.solstice.core.ui.action.onMouseRelease
import cc.lapiz.solstice.core.ui.elements.*
import cc.lapiz.solstice.core.ui.layout.*
import cc.lapiz.solstice.core.ui.style.*
import cc.lapiz.solstice.core.window.*

class MainScene : Scene() {
	override fun initUI() = UIStack(Attrib.size(Window.width().px, Window.height().px), 0f, Direction.Column, Flex.Center, Align.Center).apply {
		key = "root"
		stack(
			Attrib.size(400.px, 300.px),
			spacing = 10f,
			direction = Direction.Column,
			flex = Flex.Center,
			align = Align.Center
		) {
			button("Play") {
				println("Play button clicked")
			}
			button("Settings") {
				println("Settings button clicked")
			}
			button("Exit") {
				println("Exit button clicked")
			}

			slider("Slider: %.2f", { 0.5f }) { value ->
				println("Slider value: $value")
			}
		}
	}

	override fun nanovg() {
		NVcanvas.beginFrame(Window.width().toFloat(), Window.height().toFloat(), 1f)
		super.nanovg()
		NVcanvas.endFrame()
	}
}