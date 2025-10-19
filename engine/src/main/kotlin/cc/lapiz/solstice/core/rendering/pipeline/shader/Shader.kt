package cc.lapiz.solstice.rendering.pipeline.shader

import cc.lapiz.solstice.core.rendering.pipeline.shader.Uniform
import cc.lapiz.solstice.core.rendering.pipeline.shader.UniformScope
import cc.lapiz.solstice.core.rendering.pipeline.vertex.VertexAttributeType
import cc.lapiz.solstice.core.rendering.pipeline.vertex.VertexLayout
import cc.lapiz.solstice.core.resource.IO
import cc.lapiz.solstice.core.resource.impl.ShaderId
import cc.lapiz.solstice.core.utils.logger
import org.lwjgl.opengl.GL33C

class Shader(private val resource: ShaderId) {
	companion object {
		private val LOGGER = logger(Shader::class.java)
	}

	private var program = -1
	private val shaders = mutableMapOf<Int, Int>()
	private val uniforms = mutableMapOf<String, Uniform>()
	private lateinit var uniformScope: UniformScope
	val layout: VertexLayout by lazy { buildLayout() }

	fun init() {
		program = GL33C.glCreateProgram()
		resource.layout.shaders.forEach { shader ->
			val type = when (shader.type.lowercase()[0]) {
				'v' -> GL33C.GL_VERTEX_SHADER
				'f' -> GL33C.GL_FRAGMENT_SHADER
				else -> throw RuntimeException("Unsupported shader type: ${shader.type}")
			}
			val source = IO.getText(shader.file)
			shaders[type] = createShader(source, type)
		}

		GL33C.glLinkProgram(program)
		val status = GL33C.glGetProgrami(program, GL33C.GL_LINK_STATUS)
		if (status == GL33C.GL_FALSE) {
			LOGGER.error("Shader ${resource.layout.name} failed to link program: $program")
			LOGGER.error(GL33C.glGetProgramInfoLog(program))
			throw RuntimeException()
		}

		resource.layout.uniforms.forEach {
			uniforms[it.name] = Uniform(it.name, it.type, GL33C.glGetUniformLocation(program, it.name))
			if (uniforms[it.name]!!.location == -1) {
				LOGGER.warn("Uniform ${it.name} not found in shader ${resource.layout.name}")
			}
		}

		uniformScope = UniformScope(uniforms)
	}
	
	fun bind() {
		GL33C.glUseProgram(program)
	}

	fun unbind() {
		GL33C.glUseProgram(0)
	}

	fun uniform(scope: UniformScope.() -> Unit) {
		uniformScope.scope()
	}

	fun use(block: Shader.() -> Unit) {
		bind()
		block()
		unbind()
	}

	private fun buildLayout(): VertexLayout {
		val shaderLayout = resource.layout
		val layout = VertexLayout()
		shaderLayout.attributes.forEachIndexed { index, attr ->
			val type = when (attr.type.lowercase()) {
				"float" -> VertexAttributeType.FLOAT
				"vec2" -> VertexAttributeType.VEC2
				"vec3" -> VertexAttributeType.VEC3
				"vec4" -> VertexAttributeType.VEC4
				"int" -> VertexAttributeType.INT
				"ivec2" -> VertexAttributeType.IVEC2
				"ivec3" -> VertexAttributeType.IVEC3
				"ivec4" -> VertexAttributeType.IVEC4
				else -> throw RuntimeException("Unsupported attribute type: ${attr.type}")
			}
			layout.addAttribute(index, type)
		}

		return layout
	}

	private fun createShader(source: String, type: Int): Int {
		val shader = GL33C.glCreateShader(type)
		GL33C.glShaderSource(shader, source)
		GL33C.glCompileShader(shader)
		val status = GL33C.glGetShaderi(shader, GL33C.GL_COMPILE_STATUS)
		if (status == GL33C.GL_FALSE) {
			LOGGER.error("Shader ${resource.layout.name} failed to compile shader: $shader")
			LOGGER.error(GL33C.glGetShaderInfoLog(shader))
			throw RuntimeException()
		}
		GL33C.glAttachShader(program, shader)
		return shader
	}

	fun dispose() {
		GL33C.glDeleteProgram(program)
		shaders.values.forEach {
			GL33C.glDeleteShader(it)
		}
	}
}