package cc.lapiz.solstice.core.data

import org.joml.Vector2f

data class AABB(val min: Vector2f, val max: Vector2f) {
    fun intersects(other: AABB): Boolean {
        return max.x >= other.min.x && min.x <= other.max.x &&
               max.y >= other.min.y && min.y <= other.max.y
    }
    
    fun contains(point: Vector2f): Boolean {
        return point.x >= min.x && point.x <= max.x &&
               point.y >= min.y && point.y <= max.y
    }
}
