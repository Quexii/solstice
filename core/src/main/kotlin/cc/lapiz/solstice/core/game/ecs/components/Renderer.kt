package cc.lapiz.solstice.core.game.ecs.components

import cc.lapiz.solstice.core.data.*
import cc.lapiz.solstice.core.rendering.*
import cc.lapiz.solstice.core.rendering.pipeline.mesh.*
import cc.lapiz.solstice.core.rendering.pipeline.shader.*

abstract class Renderer {
	abstract fun makeMesh(): Mesh
	open fun preRender() {}
	open fun uniforms(scope: UniformScope) {}
	open fun posRender() {}
	protected val builder = RenderSystem.getBuilder()
	var shader: Shader? = null
	var shaderColor = floatArrayOf(1f, 1f, 1f, 1f)

	val mesh by lazy {
		shader?.let { RenderSystem.setShader { it } }
		makeMesh()
	}

	fun render(transform: Transform) {
		preRender()
		shader?.let {
			RenderSystem.setShader { it }
		}
		RenderSystem.renderMesh(mesh, transform, ::uniforms)
		posRender()
	}
}