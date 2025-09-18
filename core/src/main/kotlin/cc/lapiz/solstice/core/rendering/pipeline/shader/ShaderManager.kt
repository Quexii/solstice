package cc.lapiz.solstice.core.rendering.pipeline.shader

import cc.lapiz.solstice.core.resource.*
import cc.lapiz.solstice.core.resource.impl.*
import org.slf4j.LoggerFactory

object ShaderManager {
	private val LOGGER = LoggerFactory.getLogger(ShaderManager::class.java)

	val shaderFillRes = ShaderResource("shader_fill", "Fill Shader", "links/sh-fill.json")

	//	val shaderTextureRes = ShaderResource("shader_texture", "Texture Shader", "links/texture.json")
	lateinit var FillShader: Shader
		private set

	lateinit var TestShader: Shader
		private set

	fun loadShaders() {
		LOGGER.info("Loading shaders...")

		FillShader = loadShader(shaderFillRes)

		LOGGER.info("Loaded shaders.")
	}

	private fun loadShader(res: ShaderResource): Shader {
		ResourceManager.load(res)
		val shader = Shader(res)
		shader.init()
		return shader
	}
}