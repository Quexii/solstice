package cc.lapiz.solstice.core.game.ecs.components

import cc.lapiz.solstice.core.data.AABB
import cc.lapiz.solstice.core.data.Transform
import org.joml.Vector2f
import kotlin.math.*

class BoxCollider(var size: Vector2f = Vector2f(1f, 1f)) : Collider() {
    
    override fun contains(point: Vector2f, transform: Transform): Boolean {
        val bounds = getBounds(transform)
        return point.x >= bounds.min.x && point.x <= bounds.max.x &&
               point.y >= bounds.min.y && point.y <= bounds.max.y
    }
    
    override fun intersects(other: Collider, otherTransform: Transform, thisTransform: Transform): Boolean {
        return when (other) {
            is BoxCollider -> intersectsBox(other, otherTransform, thisTransform)
            is CircleCollider -> other.intersects(this, thisTransform, otherTransform)
            else -> false
        }
    }
    
    private fun intersectsBox(other: BoxCollider, otherTransform: Transform, thisTransform: Transform): Boolean {
        val thisBounds = getBounds(thisTransform)
        val otherBounds = other.getBounds(otherTransform)
        
        return thisBounds.intersects(otherBounds)
    }
    
    override fun getBounds(transform: Transform): AABB {
        val center = Vector2f(transform.position).add(offset)
        val scaledSize = Vector2f(size).mul(transform.scale)
        val halfSize = Vector2f(scaledSize).mul(0.5f)
        
        return AABB(
            Vector2f(center).sub(halfSize),
            Vector2f(center).add(halfSize)
        )
    }
}
