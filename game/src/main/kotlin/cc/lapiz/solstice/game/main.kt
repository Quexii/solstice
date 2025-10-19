package cc.lapiz.solstice.game

import cc.lapiz.solstice.core.Entrypoint
import cc.lapiz.solstice.core.Solstice
import cc.lapiz.solstice.core.game.components.ComponentRegistry
import cc.lapiz.solstice.game.scene.MainScene
import cc.lapiz.solstice.core.window.WindowOptions
import cc.lapiz.solstice.game.components.TestComponent

fun main(args: Array<String>) {
	Solstice.entry(Game(), args)
}

class Game : Entrypoint(MainScene()) {
	override val windowOptions = WindowOptions(
		width = 1200, height = 800, resizable = true, title = "Solstice RTS Game"
	)

	override fun start() {
		ComponentRegistry.register<TestComponent>()
	}
}