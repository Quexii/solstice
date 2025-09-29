package cc.lapiz.solstice.core.game.scenes.main

import cc.lapiz.solstice.core.game.*
import cc.lapiz.solstice.core.rendering.nanovg.*
import cc.lapiz.solstice.core.window.*

class MainScene : Scene() {
	override fun nanovg() {
		NVcanvas.beginFrame(Window.width().toFloat(), Window.height().toFloat(), 1f)
		super.nanovg()
		NVcanvas.endFrame()
	}
}