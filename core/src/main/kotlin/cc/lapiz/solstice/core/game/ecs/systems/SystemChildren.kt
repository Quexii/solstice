package cc.lapiz.solstice.core.game.ecs.systems

import cc.lapiz.solstice.core.data.*
import cc.lapiz.solstice.core.game.ecs.*
import cc.lapiz.solstice.core.game.ecs.components.*

class SystemChildren : System {
	override fun update(ecs: ECS, dt: Float) {
		ecs.allEntities().forEach { entity ->
			val transform = ecs.getComponent(entity, Transform::class.java)
			val parentComp = ecs.getComponent(entity, Parent::class.java)
			val childrenComp = ecs.getComponent(entity, Children::class.java)

			if (parentComp != null) {
				val parentTransform = ecs.getComponent(parentComp.parentId, Transform::class.java)
				if (parentTransform != null && transform != null) {
					parentTransform.addChild(transform)
				}
			}

			if (childrenComp != null && transform != null) {
				childrenComp.childIds.forEach { childEntity ->
					val childTransform = ecs.getComponent(childEntity, Transform::class.java)
					if (childTransform != null) {
						transform.addChild(childTransform)
					}
				}
			}
		}
	}
}