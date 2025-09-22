package cc.lapiz.solstice.core.game.ecs.systems

import cc.lapiz.solstice.core.data.Transform
import cc.lapiz.solstice.core.game.ecs.ECS
import cc.lapiz.solstice.core.game.ecs.System
import cc.lapiz.solstice.core.game.ecs.components.SpriteRenderer

class SystemRenderer : System {
	override fun render(ecs: ECS) {
		ecs.allEntities().forEach { entity ->
			val transform = ecs.getComponent(entity, Transform::class.java)
			val renderer = ecs.getComponent(entity, SpriteRenderer::class.java)

			if (transform != null && renderer != null) {
				renderer.render(transform)
			}
		}
	}
}