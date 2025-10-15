package cc.lapiz.solstice.rendering.pipeline.shader

import cc.lapiz.solstice.resource.*
import cc.lapiz.solstice.resource.impl.*
import org.slf4j.*

object ShaderManager {
	private val LOGGER = LoggerFactory.getLogger(ShaderManager::class.java)

	private val shaderFillRes = ShaderId("shader_fill", "links/sh_fill.json")
	private val shaderUVTest = ShaderId("shader_uvtest", "links/sh_uvtest.json")
	private val shaderSpriteRes = ShaderId("shader_sprite", "links/sh_sprite.json")
	private val shaderCircleRes = ShaderId("shader_circle", "links/sh_circle.json")

	lateinit var FillShader: Shader
		private set

	lateinit var UVTestShader: Shader
		private set

	lateinit var SpriteShader: Shader
		private set

	lateinit var CircleShader: Shader
		private set

	fun loadShaders() {
		LOGGER.info("Loading shaders...")

		FillShader = loadShader(shaderFillRes)
		UVTestShader = loadShader(shaderUVTest)
		SpriteShader = loadShader(shaderSpriteRes)
		CircleShader = loadShader(shaderCircleRes)
		LOGGER.info("Loaded shaders.")
	}

	private fun loadShader(res: ShaderId): Shader {
		ResourceManager.load(res)
		val shader = Shader(res)
		shader.init()
		return shader
	}
}