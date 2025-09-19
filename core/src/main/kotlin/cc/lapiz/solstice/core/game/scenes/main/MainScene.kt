package cc.lapiz.solstice.core.game.scenes.main

import cc.lapiz.solstice.core.game.*
import cc.lapiz.solstice.core.rendering.nanovg.*
import cc.lapiz.solstice.core.ui.*
import cc.lapiz.solstice.core.ui.style.*
import cc.lapiz.solstice.core.ui.widget.Button
import cc.lapiz.solstice.core.window.*

class MainScene : Scene() {
	override val ui = Root(alignment = Alignment.Center) {
		Button(Modifier.size(200f, 50f), "Click Me 1") {
			println("Button 1 Clicked!")
		}
		Button(Modifier.size(200f, 50f), "Click Me 2") {
			println("Button 2 Clicked!")
		}
		Button(Modifier.size(200f, 50f), "Click Me 3") {
			println("Button 3 Clicked!")
		}
	}

	override fun nanovg() {
		NVcanvas.beginFrame(Window.width().toFloat(), Window.height().toFloat(), 1f)
		super.nanovg()
		NVcanvas.endFrame()
	}
}