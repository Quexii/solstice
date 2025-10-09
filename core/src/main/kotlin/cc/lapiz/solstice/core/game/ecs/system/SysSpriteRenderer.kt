package cc.lapiz.solstice.core.game.ecs.system

import cc.lapiz.solstice.core.data.*
import cc.lapiz.solstice.core.game.ecs.component.impl.*
import cc.lapiz.solstice.core.game.ecs.entity.*
import cc.lapiz.solstice.core.rendering.*
import cc.lapiz.solstice.core.rendering.pipeline.mesh.*

class SysSpriteRenderer : System {
	override fun render(sys: ECS) {
		for (result in sys.query(SpriteRenderer::class, Transform::class)) {
			val transform = result.require<Transform>()
			val spriteRenderer = result.require<SpriteRenderer>()
			if (spriteRenderer.sprite == null) continue
			RenderSystem.bindTexture(spriteRenderer.sprite!!.textureId())
			RenderSystem.renderMesh(Meshes.UVRect, transform) {
				sampler2D("Texture", 0)
				vec4("Color", spriteRenderer.color.r, spriteRenderer.color.g, spriteRenderer.color.b, spriteRenderer.color.a)
			}
		}
	}
}