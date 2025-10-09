package cc.lapiz.solstice.core.game.ecs.component.impl.collider

import cc.lapiz.solstice.core.data.*
import kotlinx.serialization.*

@Serializable
data class CircleCollider(
	@Contextual override val position: Vector2,
    var radius: Float
) : Collider2D {

    override fun intersects(other: Collider2D): Boolean {
        return when (other) {
            is CircleCollider -> intersectsCircle(other)
            is BoxCollider -> other.intersects(this)
            else -> false
        }
    }

    private fun intersectsCircle(other: CircleCollider): Boolean {
        val distSq = position.distanceSquared(other.position)
        val radiusSum = radius + other.radius
        return distSq <= radiusSum * radiusSum
    }
}
