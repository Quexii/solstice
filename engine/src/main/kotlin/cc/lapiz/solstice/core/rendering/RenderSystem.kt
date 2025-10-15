package cc.lapiz.solstice.rendering

import cc.lapiz.solstice.data.*
import cc.lapiz.solstice.font.*
import cc.lapiz.solstice.rendering.nanovg.*
import cc.lapiz.solstice.rendering.nanovg.platform.*
import cc.lapiz.solstice.rendering.pipeline.mesh.*
import cc.lapiz.solstice.rendering.pipeline.shader.*
import cc.lapiz.solstice.window.Display
import org.lwjgl.nanovg.NanoVGGL3
import org.lwjgl.opengl.GL33C

object RenderSystem {
	private var currentShader: Shader? = null
	private var camera = Camera()
	private var framebuffer = Framebuffer()

	val Camera: Camera get() = camera

	fun init() {
		NVcanvas.init(NVgl3(), NanoVGGL3.NVG_ANTIALIAS or NanoVGGL3.NVG_STENCIL_STROKES)
		framebuffer.init(Display.width(), Display.height())

		ShaderManager.loadShaders()
		Meshes.init()
		camera.projectWorld()
		FontManager.loadFonts()
	}

	fun framebuffer(): Framebuffer = framebuffer

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
		GL33C.glEnable(GL33C.GL_BLEND)
		GL33C.glBlendFunc(GL33C.GL_SRC_ALPHA, GL33C.GL_ONE_MINUS_SRC_ALPHA)
	}

	fun disableAlpha() {
		GL33C.glDisable(GL33C.GL_BLEND)
	}

	fun enableDepth() {
		GL33C.glEnable(GL33C.GL_DEPTH_TEST)
	}

	fun disableDepth() {
		GL33C.glDisable(GL33C.GL_DEPTH_TEST)
	}

	fun clear(r: Float, g: Float, b: Float, a: Float) {
		GL33C.glClearColor(r, g, b, a)
		GL33C.glClear(GL33C.GL_COLOR_BUFFER_BIT or GL33C.GL_DEPTH_BUFFER_BIT)
	}
}