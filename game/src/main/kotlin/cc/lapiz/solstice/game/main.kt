package cc.lapiz.solstice.game

import cc.lapiz.solstice.core.Entrypoint
import cc.lapiz.solstice.core.Solstice
import cc.lapiz.solstice.game.scene.MainScene
import cc.lapiz.solstice.resource.ResourceManager
import cc.lapiz.solstice.resource.impl.SpriteId
import cc.lapiz.solstice.window.WindowOptions

fun main(args: Array<String>) {
	Solstice.entry(Game(), args)
}

class Game : Entrypoint(MainScene()) {
	override val windowOptions = WindowOptions(
		width = 1200, height = 800, resizable = true, title = "Solstice RTS Game"
	)

	override fun start() {
		ResourceManager.load(SpriteId("test", "structure_alloy_rock.png"))
	}
}