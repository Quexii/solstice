package cc.lapiz.solstice.core.game.ecs.components

import cc.lapiz.solstice.core.data.AABB
import cc.lapiz.solstice.core.data.Transform
import org.joml.Vector2f
import kotlin.math.*

class CircleCollider(var radius: Float = 1f) : Collider() {
    
    override fun contains(point: Vector2f, transform: Transform): Boolean {
        val center = Vector2f(transform.position).add(offset)
        val scale = min(transform.scale.x, transform.scale.y)
        val scaledRadius = radius * scale
        return point.distance(center) <= scaledRadius
    }
    
    override fun intersects(other: Collider, otherTransform: Transform, thisTransform: Transform): Boolean {
        return when (other) {
            is CircleCollider -> intersectsCircle(other, otherTransform, thisTransform)
            is BoxCollider -> intersectsBox(other, otherTransform, thisTransform)
            else -> false
        }
    }
    
    private fun intersectsCircle(other: CircleCollider, otherTransform: Transform, thisTransform: Transform): Boolean {
        val thisCenter = Vector2f(thisTransform.position).add(offset)
        val otherCenter = Vector2f(otherTransform.position).add(other.offset)
        
        val thisRadius = radius * min(thisTransform.scale.x, thisTransform.scale.y)
        val otherRadius = other.radius * min(otherTransform.scale.x, otherTransform.scale.y)
        
        return thisCenter.distance(otherCenter) <= thisRadius + otherRadius
    }
    
    private fun intersectsBox(box: BoxCollider, boxTransform: Transform, thisTransform: Transform): Boolean {
        val bounds = box.getBounds(boxTransform)
        val center = Vector2f(thisTransform.position).add(offset)
        val scaledRadius = radius * min(thisTransform.scale.x, thisTransform.scale.y)
        
        val closest = Vector2f(
            center.x.coerceIn(bounds.min.x, bounds.max.x),
            center.y.coerceIn(bounds.min.y, bounds.max.y)
        )
        
        return center.distance(closest) <= scaledRadius
    }
    
    override fun getBounds(transform: Transform): AABB {
        val center = Vector2f(transform.position).add(offset)
        val scaledRadius = radius * min(transform.scale.x, transform.scale.y)
        return AABB(
            Vector2f(center.x - scaledRadius, center.y - scaledRadius),
            Vector2f(center.x + scaledRadius, center.y + scaledRadius)
        )
    }
}
