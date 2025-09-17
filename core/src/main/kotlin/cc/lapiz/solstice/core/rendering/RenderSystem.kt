package cc.lapiz.solstice.core.rendering

import cc.lapiz.solstice.core.data.*
import cc.lapiz.solstice.core.rendering.pipeline.mesh.*
import cc.lapiz.solstice.core.rendering.pipeline.shader.*
import org.lwjgl.*

object RenderSystem {
	private var currentShader: Shader? = null
	private var camera = Camera()
	private var viewBuffer = BufferUtils.createFloatBuffer(16)
	private val transform = Transform()

	fun init() {
		ShaderManager.loadShaders()
	}

	fun setShader(shader: ShaderManager.() -> Shader) {
		this.currentShader = shader(ShaderManager)
	}

	fun currentShader(): Shader? = currentShader

	fun renderMesh(mesh: Mesh) {
		currentShader?.use {
			uniform {
				mat4("ProjViewMatrix", camera.getMatrix().get(viewBuffer))
				mat4("ModelMatrix", transform.store())
			}
			mesh.render()
		}
	}

	fun camera() = camera
}