package cc.lapiz.solstice.core.rendering.pipeline.shader

import cc.lapiz.solstice.core.resource.*
import cc.lapiz.solstice.core.resource.impl.*
import org.slf4j.*

object ShaderManager {
	private val LOGGER = LoggerFactory.getLogger(ShaderManager::class.java)

	private val shaderFillRes = ShaderResource("shader_fill", "Fill Shader", "links/sh_fill.json")
	private val shaderUVTest = ShaderResource("shader_uvtest", "UV Test Shader", "links/sh_uvtest.json")
	private val shaderSpriteRes = ShaderResource("shader_sprite", "Sprite Shader", "links/sh_sprite.json")
	private val shaderCircleRes = ShaderResource("shader_circle", "Circle Shader", "links/sh_circle.json")

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

	private fun loadShader(res: ShaderResource): Shader {
		ResourceManager.load(res)
		val shader = Shader(res)
		shader.init()
		return shader
	}
}