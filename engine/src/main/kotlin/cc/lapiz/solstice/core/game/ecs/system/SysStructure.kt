package cc.lapiz.solstice.game.ecs.system

import cc.lapiz.solstice.game.ecs.component.impl.*
import cc.lapiz.solstice.game.ecs.component.impl.structure.Structure
import cc.lapiz.solstice.game.ecs.entity.*

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