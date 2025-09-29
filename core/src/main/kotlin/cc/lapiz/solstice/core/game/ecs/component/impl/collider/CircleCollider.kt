package cc.lapiz.solstice.core.game.ecs.component.impl.collider

import org.joml.Vector2f

data class CircleCollider(
    override val position: Vector2f,
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
