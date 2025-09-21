package cc.lapiz.solstice.core.game.scenes.main

import cc.lapiz.solstice.core.game.*
import cc.lapiz.solstice.core.rendering.nanovg.*
import cc.lapiz.solstice.core.ui.elements.*
import cc.lapiz.solstice.core.ui.layout.*
import cc.lapiz.solstice.core.ui.style.*
import cc.lapiz.solstice.core.window.*

class MainScene : Scene() {

	override fun initUI() = UIStack(Attrib.size(Window.width().toFloat(), Window.height().toFloat()), 0f, Direction.Column, Flex.Center, Align.Center).apply {
		key = "root"
		stack(
			Attrib.size(400f, 300f),
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
		}
	}

	override fun nanovg() {
		NVcanvas.beginFrame(Window.width().toFloat(), Window.height().toFloat(), 1f)
		super.nanovg()
		NVcanvas.endFrame()
	}
}