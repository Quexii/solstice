package cc.lapiz.solstice.core.game.ecs.component.impl.collider

import cc.lapiz.solstice.core.data.*

interface Collider2D {
	val position: Vector2
    fun intersects(other: Collider2D): Boolean
}
