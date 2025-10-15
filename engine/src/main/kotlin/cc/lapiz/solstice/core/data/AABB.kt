package cc.lapiz.solstice.data

import org.joml.*

data class AABB(val min: cc.lapiz.solstice.data.Vector2, val max: cc.lapiz.solstice.data.Vector2) {
    fun intersects(other: AABB): Boolean {
        return max.x >= other.min.x && min.x <= other.max.x &&
               max.y >= other.min.y && min.y <= other.max.y
    }
    
    fun contains(point: cc.lapiz.solstice.data.Vector2): Boolean {
        return point.x >= min.x && point.x <= max.x &&
               point.y >= min.y && point.y <= max.y
    }
}
