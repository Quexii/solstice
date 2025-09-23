package cc.lapiz.solstice.core.game.ecs.components

import cc.lapiz.solstice.core.data.*
import cc.lapiz.solstice.core.rendering.pipeline.mesh.*
import cc.lapiz.solstice.core.rendering.pipeline.shader.*
import cc.lapiz.solstice.core.utils.*
import kotlin.math.*

class CircleRenderer : Renderer() {
	private val LOGGER = logger(SpriteRenderer::class.java)

	companion object {
		val ModeFill = 1
		val ModeStroke = 2
		val ModeShadow = 3
	}

	var mode = ModeStroke

	override fun preRender() {
		shader = ShaderManager.CircleShader
	}

	override fun makeMesh(): Mesh {
		val size = 1f

		builder.begin(Mesh.Mode.TRIANGLES)
		builder.pos(-size, -size).tex(0f, 0f).endVertex()
		builder.pos(size, -size).tex(1f, 0f).endVertex()
		builder.pos(size, size).tex(1f, 1f).endVertex()
		builder.pos(size, size).tex(1f, 1f).endVertex()
		builder.pos(-size, size).tex(0f, 1f).endVertex()
		builder.pos(-size, -size).tex(0f, 0f).endVertex()
		return builder.build()
	}

	override fun uniforms(scope: UniformScope, transform: Transform) {
		scope.float("MaxSize", min(transform.scale.x, transform.scale.y) * 12f)
		scope.vec4("Color", shaderColor[0], shaderColor[1], shaderColor[2], shaderColor[3])
		scope.int("Mode", mode)
	}
}