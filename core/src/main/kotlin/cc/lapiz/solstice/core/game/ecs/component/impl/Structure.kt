package cc.lapiz.solstice.core.game.ecs.component.impl

import cc.lapiz.solstice.core.resource.impl.SpriteResource
import kotlinx.serialization.Serializable


class Structure(val name: String, val id: Int, val width: Int, val height: Int, var sprite: SpriteResource) {
	var dirty: Boolean = true

	fun toSprite(): SpriteResource {
		return sprite
	}
}