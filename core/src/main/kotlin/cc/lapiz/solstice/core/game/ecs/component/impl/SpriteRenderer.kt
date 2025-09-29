package cc.lapiz.solstice.core.game.ecs.component.impl

import cc.lapiz.solstice.core.resource.impl.*
import org.joml.*

class SpriteRenderer(var sprite: SpriteResource?, val color: Vector4f) {
	constructor(sprite: SpriteResource?) : this(sprite, Vector4f(1f))
}