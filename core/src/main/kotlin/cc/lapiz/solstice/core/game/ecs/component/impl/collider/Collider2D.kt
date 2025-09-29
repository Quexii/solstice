package cc.lapiz.solstice.core.game.ecs.component.impl.collider

import org.joml.Vector2f

interface Collider2D {
    val position: Vector2f
    fun intersects(other: Collider2D): Boolean
}
