package cc.lapiz.solstice.core.game.scenes.game

import cc.lapiz.solstice.core.data.*
import cc.lapiz.solstice.core.debug.*
import cc.lapiz.solstice.core.game.*
import cc.lapiz.solstice.core.game.ecs.component.impl.*
import cc.lapiz.solstice.core.game.ecs.component.impl.collider.CircleCollider
import cc.lapiz.solstice.core.game.ecs.system.*
import cc.lapiz.solstice.core.game.level.Level

class GameScene : Scene() {
	private val level = Level("Test Level", 100, 100, ecs)

	override fun update(delta: Float) {
		super.update(delta)
		level.update(delta)
		DBG.right("e: ${ecs.query().toList().size}")
	}

	override fun render() {
		super.render()
		level.render()
	}
}