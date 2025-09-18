package cc.lapiz.solstice.core.rendering

import cc.lapiz.solstice.core.data.*
import cc.lapiz.solstice.core.font.FontFamily
import cc.lapiz.solstice.core.rendering.pipeline.mesh.*
import cc.lapiz.solstice.core.rendering.pipeline.shader.*
import cc.lapiz.solstice.core.resource.ResourceManager
import cc.lapiz.solstice.core.resource.impl.FontResource

object RenderSystem {
	private var currentShader: Shader? = null
	private var defaultFont: FontFamily? = null
	private var camera = Camera()
	private val transform = Transform()

	val Camera: Camera get() = camera
	val DefaultFont get() = defaultFont ?: throw IllegalStateException("Default font not initialized!")

	fun init() {
		ShaderManager.loadShaders()
		camera.projectWorld()
		val res = FontResource("font_default", "Default Font", "links/font-default.json")
		ResourceManager.load(res)
		defaultFont = FontFamily(res.layout)
	}

	fun setShader(shader: ShaderManager.() -> Shader) {
		this.currentShader = shader(ShaderManager)
	}

	fun currentShader(): Shader? = currentShader

	fun renderMesh(mesh: Mesh) {
		currentShader?.use {
			uniform {
				mat4("ProjMatrix", camera.projectionBuffer)
				mat4("ModelMatrix", transform.store())
			}
			mesh.render()
		}
	}
}