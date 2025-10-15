package cc.lapiz.solstice.game.ecs.component.impl

import cc.lapiz.solstice.data.*
import cc.lapiz.solstice.resource.*
import cc.lapiz.solstice.resource.impl.*
import kotlinx.serialization.*

@Serializable
class SpriteRenderer(
	var spriteId: String,
	val color: Color
) {
	@Transient
	var sprite: SpriteId? = ResourceManager.get(spriteId)

	constructor(sprite: SpriteId?, color: Color) : this(
		sprite?.id ?: "",
		color
	) {
		this.sprite = sprite
	}

	constructor(sprite: SpriteId?) : this(sprite, Color(1f))
}
