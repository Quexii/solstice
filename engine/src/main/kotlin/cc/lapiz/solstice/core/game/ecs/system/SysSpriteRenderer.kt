package cc.lapiz.solstice.game.ecs.system

import cc.lapiz.solstice.data.*
import cc.lapiz.solstice.game.ecs.component.impl.*
import cc.lapiz.solstice.game.ecs.entity.*
import cc.lapiz.solstice.rendering.*
import cc.lapiz.solstice.rendering.pipeline.mesh.*
import cc.lapiz.solstice.rendering.texture.*

class SysSpriteRenderer : System {
	override fun render(sys: ECS) {
		for (result in sys.query(SpriteRenderer::class, Transform::class)) {
			val transform = result.require<Transform>()
			val spriteRenderer = result.require<SpriteRenderer>()
			if (spriteRenderer.sprite == null) continue
			TextureUtil.bindTexture(spriteRenderer.sprite!!.textureId())
			RenderSystem.renderMesh(Meshes.UVRect, transform) {
				sampler2D("Texture", 0)
				vec4("Color", spriteRenderer.color.r, spriteRenderer.color.g, spriteRenderer.color.b, spriteRenderer.color.a)
			}
		}
	}
}