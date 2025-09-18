package cc.lapiz.solstice.core.game.scenes.main

import cc.lapiz.solstice.core.event.InputEvent
import cc.lapiz.solstice.core.game.*
import cc.lapiz.solstice.core.ui.button

class MainScene : Scene() {
	init {
		ui.build {
			button("Test Button", 100f, 100f, 200f, 50f) {
				println("Button Clicked!")
			}
		}
	}

	override fun onInput(evnet: InputEvent) {
		ui.onInput(evnet)
	}

	override fun ui() {
		ui.draw()
	}
}