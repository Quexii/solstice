package cc.lapiz.solstice.core.game.ecs.component.impl.collider

import org.joml.Vector2f

data class BoxCollider(
    override val position: Vector2f,
    var width: Float,
    var height: Float
) : Collider2D {

    override fun intersects(other: Collider2D): Boolean {
        return when (other) {
            is BoxCollider -> intersectsBox(other)
            is CircleCollider -> intersectsCircle(other)
            else -> false
        }
    }

    private fun intersectsBox(other: BoxCollider): Boolean {
        return !(position.x + width / 2 < other.position.x - other.width / 2 ||
                 position.x - width / 2 > other.position.x + other.width / 2 ||
                 position.y + height / 2 < other.position.y - other.height / 2 ||
                 position.y - height / 2 > other.position.y + other.height / 2)
    }

    private fun intersectsCircle(circle: CircleCollider): Boolean {
        val closestX = circle.position.x.coerceIn(
            position.x - width / 2,
            position.x + width / 2
        )
        val closestY = circle.position.y.coerceIn(
            position.y - height / 2,
            position.y + height / 2
        )
        val dx = circle.position.x - closestX
        val dy = circle.position.y - closestY
        return (dx * dx + dy * dy) < (circle.radius * circle.radius)
    }
}
