package cc.lapiz.solstice.core.game.ecs.components

import cc.lapiz.solstice.core.data.AABB
import cc.lapiz.solstice.core.data.Transform
import org.joml.Vector2f

abstract class Collider {
    var isTrigger = false
    var enabled = true
    var layer = 0
    var offset = Vector2f(0f, 0f)
    
    abstract fun contains(point: Vector2f, transform: Transform): Boolean
    abstract fun intersects(other: Collider, otherTransform: Transform, thisTransform: Transform): Boolean
    abstract fun getBounds(transform: Transform): AABB
}
