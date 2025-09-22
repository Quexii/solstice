package cc.lapiz.solstice.core.game.ecs.components

import cc.lapiz.solstice.core.data.Transform
import cc.lapiz.solstice.core.rendering.RenderSystem
import cc.lapiz.solstice.core.rendering.pipeline.mesh.Mesh
import cc.lapiz.solstice.core.rendering.pipeline.mesh.MeshBuilder

class TestRenderer {
	private val builder = MeshBuilder()

	var mesh: Mesh
	var shaderColor: FloatArray = floatArrayOf(1f, 1f, 1f, 1f)

	init {
		RenderSystem.setShader { FillShader }
		builder.begin(Mesh.Mode.TRIANGLES)
		builder.pos(-0.5f, -0.5f).color(shaderColor[0], shaderColor[1], shaderColor[2], shaderColor[3]).endVertex()
		builder.pos(0.5f, -0.5f).color(shaderColor[0], shaderColor[1], shaderColor[2], shaderColor[3]).endVertex()
		builder.pos(0.5f, 0.5f).color(shaderColor[0], shaderColor[1], shaderColor[2], shaderColor[3]).endVertex()
		builder.pos(0.5f, 0.5f).color(shaderColor[0], shaderColor[1], shaderColor[2], shaderColor[3]).endVertex()
		builder.pos(-0.5f, 0.5f).color(shaderColor[0], shaderColor[1], shaderColor[2], shaderColor[3]).endVertex()
		builder.pos(-0.5f, -0.5f).color(shaderColor[0], shaderColor[1], shaderColor[2], shaderColor[3]).endVertex()
		mesh = builder.build()
	}

	fun drawQuad(transform: Transform) {
		RenderSystem.setShader { FillShader }
		RenderSystem.renderMesh(mesh, transform)
	}
}