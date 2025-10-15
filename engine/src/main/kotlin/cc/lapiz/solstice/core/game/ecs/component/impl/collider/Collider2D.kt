package cc.lapiz.solstice.game.ecs.component.impl.collider

import cc.lapiz.solstice.data.*

interface Collider2D {
	val position: Vector2
    fun intersects(other: Collider2D): Boolean
}
