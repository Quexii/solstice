package cc.lapiz.solstice.game.ecs.component.impl.structure

import cc.lapiz.solstice.resource.impl.SpriteId


class Structure(val name: String, val id: Int, val width: Int, val height: Int, var sprite: SpriteId) {
	var dirty: Boolean = true

	fun toSprite(): SpriteId {
		return sprite
	}
}