package cc.lapiz.solstice.core.rendering

import cc.lapiz.solstice.core.data.*
import cc.lapiz.solstice.core.font.FontFamily
import cc.lapiz.solstice.core.font.FontManager
import cc.lapiz.solstice.core.rendering.pipeline.mesh.*
import cc.lapiz.solstice.core.rendering.pipeline.shader.*
import cc.lapiz.solstice.core.rendering.platform.Graphics
import cc.lapiz.solstice.core.resource.ResourceManager
import cc.lapiz.solstice.core.resource.impl.FontResource

object RenderSystem {
	private var currentShader: Shader? = null
	private var defaultFont: FontFamily? = null
	private var camera = Camera()

	val Camera: Camera get() = camera

	fun init() {
		ShaderManager.loadShaders()
		Meshes.init()
		camera.projectWorld()
		FontManager.loadFonts()
	}

	fun setShader(shader: ShaderManager.() -> Shader) {
		this.currentShader = shader(ShaderManager)
	}

	fun currentShader(): Shader? = currentShader

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

	fun enableDepth() {
		Graphics.enable(Graphics.DEPTH_TEST)
	}

	fun disableDepth() {
		Graphics.disable(Graphics.DEPTH_TEST)
	}

	fun clear(r: Float, g: Float, b: Float, a: Float) {
		Graphics.clearColor(r, g, b, a)
		Graphics.clear(Graphics.COLOR_BUFFER_BIT or Graphics.DEPTH_BUFFER_BIT)
	}

	fun bindTexture(textureId: Int, unit: Int = 0) {
		Graphics.activeTexture(Graphics.TEXTURE0 + unit)
		Graphics.bindTexture(Graphics.TEXTURE_2D, textureId)
	}
}