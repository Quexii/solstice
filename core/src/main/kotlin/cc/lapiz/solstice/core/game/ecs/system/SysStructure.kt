package cc.lapiz.solstice.core.game.ecs.system

import cc.lapiz.solstice.core.game.ecs.component.impl.*
import cc.lapiz.solstice.core.game.ecs.entity.*

class SysStructure: System {
	override fun update(sys: ECS, delta: Float) {
		for (result in sys.query(SpriteRenderer::class, Structure::class)) {
			val sprite = result.require<SpriteRenderer>()
			val gridStructure = result.require<Structure>()

			if (gridStructure.dirty) {
				sprite.sprite = gridStructure.toSprite()
				gridStructure.dirty = false
			}
		}
	}
}