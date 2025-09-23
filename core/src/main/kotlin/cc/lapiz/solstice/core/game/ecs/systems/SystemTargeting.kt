package cc.lapiz.solstice.core.game.ecs.systems

import cc.lapiz.solstice.core.data.*
import cc.lapiz.solstice.core.game.ecs.*
import cc.lapiz.solstice.core.game.ecs.components.*
import org.joml.Vector2f
import kotlin.math.*

class SystemTargeting: System {
	override fun update(ecs: ECS, dt: Float) {
		ecs.allEntities().forEach { entity ->
			val transform = ecs.getComponent(entity, Transform::class.java)
			val goToTarget = ecs.getComponent(entity, GoToTarget::class.java)

			if (goToTarget != null && transform != null && goToTarget.targets.isNotEmpty()) {
				val currentTarget = goToTarget.targets.first()
				val direction = currentTarget.position.sub(transform.position, Vector2f()).normalize()
				val speed = 16f
				transform.position.add(direction.mul(speed * dt))

				val targetRotation = atan2(direction.y, direction.x) * 180 / PI.toFloat() + 90f
				val rotationSpeed = 360f

				var angleDiff = targetRotation - transform.rotation
				if (angleDiff > 180f) angleDiff -= 360f
				if (angleDiff < -180f) angleDiff += 360f

				val rotationStep = rotationSpeed * dt
				if (abs(angleDiff) <= rotationStep) {
					transform.rotation = targetRotation
				} else {
					transform.rotation += sign(angleDiff) * rotationStep
				}

				if (transform.position.distance(currentTarget.position) < 0.1f) {
					goToTarget.targets.removeAt(0)
				}
			}
		}
	}
}
