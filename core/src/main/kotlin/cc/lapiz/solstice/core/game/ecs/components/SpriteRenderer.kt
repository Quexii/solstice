package cc.lapiz.solstice.core.game.ecs.components

import cc.lapiz.solstice.core.data.Transform
import cc.lapiz.solstice.core.rendering.pipeline.mesh.*
import cc.lapiz.solstice.core.rendering.pipeline.shader.UniformScope
import cc.lapiz.solstice.core.rendering.platform.Graphics
import cc.lapiz.solstice.core.resource.impl.SpriteResource
import cc.lapiz.solstice.core.utils.logger

class SpriteRenderer: Renderer() {
	var sprite: SpriteResource? = null

	private val LOGGER = logger(SpriteRenderer::class.java)

	override fun makeMesh(): Mesh {
		builder.begin(Mesh.Mode.TRIANGLES)
		builder.pos(-0.5f, -0.5f).tex(0f, 0f).endVertex()
		builder.pos(0.5f, -0.5f).tex(1f, 0f).endVertex()
		builder.pos(0.5f, 0.5f).tex(1f, 1f).endVertex()
		builder.pos(0.5f, 0.5f).tex(1f, 1f).endVertex()
		builder.pos(-0.5f, 0.5f).tex(0f, 1f).endVertex()
		builder.pos(-0.5f, -0.5f).tex(0f, 0f).endVertex()
		return builder.build()
	}

	override fun preRender() {
		val textureId = sprite?.textureId() ?: -1
		if (textureId == -1) {
			LOGGER.warn("No valid texture bound!")
		}
		Graphics.activeTexture(Graphics.TEXTURE0)
		Graphics.bindTexture(Graphics.TEXTURE_2D, textureId)
	}

	override fun uniforms(scope: UniformScope, transform: Transform) {
		scope.sampler2D("Texture", 0)
		scope.vec4("Color", shaderColor[0], shaderColor[1], shaderColor[2], shaderColor[3])
	}
}