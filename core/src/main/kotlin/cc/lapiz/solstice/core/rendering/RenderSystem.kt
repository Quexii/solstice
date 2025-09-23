package cc.lapiz.solstice.core.rendering

import cc.lapiz.solstice.core.data.*
import cc.lapiz.solstice.core.font.FontFamily
import cc.lapiz.solstice.core.rendering.pipeline.mesh.*
import cc.lapiz.solstice.core.rendering.pipeline.shader.*
import cc.lapiz.solstice.core.rendering.platform.Graphics
import cc.lapiz.solstice.core.resource.ResourceManager
import cc.lapiz.solstice.core.resource.impl.FontResource

object RenderSystem {
	private var currentShader: Shader? = null
	private var defaultFont: FontFamily? = null
	private var camera = Camera()
	private val globalBuilder = MeshBuilder()

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

	fun getBuilder(): MeshBuilder = globalBuilder

	fun renderMesh(mesh: Mesh, transform: Transform, extraUniforms: UniformScope.() -> Unit = {}) {
		currentShader?.use {
			uniform {
				mat4("ProjMatrix", camera.projectionBuffer)
				mat4("ModelMatrix", transform.store())
				extraUniforms()
			}
			mesh.render()
		}
	}

	fun enableAlpha() {
		Graphics.enable(Graphics.BLEND)
		Graphics.blendFunc(Graphics.SRC_ALPHA, Graphics.ONE_MINUS_SRC_ALPHA)
	}

	fun disableAlpha() {
		Graphics.disable(Graphics.BLEND)
	}

	fun clear(r: Float, g: Float, b: Float, a: Float) {
		Graphics.clearColor(r, g, b, a)
		Graphics.clear(Graphics.COLOR_BUFFER_BIT or Graphics.DEPTH_BUFFER_BIT)
	}
}