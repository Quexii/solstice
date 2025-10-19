package cc.lapiz.solstice.core.data

data class AABB(val min: Vector2, val max: Vector2) {
    fun intersects(other: AABB): Boolean {
        return max.x >= other.min.x && min.x <= other.max.x &&
               max.y >= other.min.y && min.y <= other.max.y
    }
    
    fun contains(point: Vector2): Boolean {
        return point.x >= min.x && point.x <= max.x &&
               point.y >= min.y && point.y <= max.y
    }
}
