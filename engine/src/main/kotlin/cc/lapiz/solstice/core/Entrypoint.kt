package cc.lapiz.solstice.core

import cc.lapiz.solstice.core.game.Scene
import cc.lapiz.solstice.core.window.WindowOptions

abstract class Entrypoint(val mainScene: Scene) {
	open val windowOptions = WindowOptions()

	abstract fun start()
}