package cc.lapiz.solstice.core.game.ecs.component.impl

import cc.lapiz.solstice.core.data.*
import cc.lapiz.solstice.core.resource.*
import cc.lapiz.solstice.core.resource.impl.*
import kotlinx.serialization.*

@Serializable
class SpriteRenderer(
	var spriteId: String,
	val color: Color
) {
	@Transient
	var sprite: SpriteResource? = ResourceManager.get(spriteId)

	constructor(sprite: SpriteResource?, color: Color) : this(
		sprite?.id ?: "",
		color
	) {
		this.sprite = sprite
	}

	constructor(sprite: SpriteResource?) : this(sprite, Color(1f))
}
