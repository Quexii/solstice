package cc.lapiz.solstice.core.game.scenes.game

import cc.lapiz.solstice.core.dev.*
import cc.lapiz.solstice.core.game.*
import cc.lapiz.solstice.core.game.level.Level

class GameScene : Scene() {
	private val level = Level("Test Level", 100, 100, ecs)

	override fun update(delta: Float) {
		super.update(delta)
		level.update(delta)
		F3.right("e: ${ecs.query().toList().size}")
	}

	override fun render() {
		super.render()
		level.render()
	}
}