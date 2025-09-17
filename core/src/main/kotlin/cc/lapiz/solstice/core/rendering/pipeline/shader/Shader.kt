package cc.lapiz.solstice.core.rendering.pipeline.shader

import cc.lapiz.solstice.core.rendering.pipeline.vertex.VertexAttributeType
import cc.lapiz.solstice.core.rendering.pipeline.vertex.VertexLayout
import cc.lapiz.solstice.core.rendering.platform.Gr
import cc.lapiz.solstice.core.rendering.platform.Graphics
import cc.lapiz.solstice.core.resource.IO
import cc.lapiz.solstice.core.resource.impl.ShaderResource
import cc.lapiz.solstice.core.utils.logger

class Shader(private val resource: ShaderResource) {
	companion object {
		private val LOGGER = logger(Shader::class.java)
	}

	private var program = -1
	private val shaders = mutableMapOf<Int, Int>()
	private val uniforms = mutableMapOf<String, Int>()
	private lateinit var uniformScope: UniformScope
	val layout: VertexLayout by lazy { buildLayout() }

	fun init() {
		program = Graphics.createProgram()
		resource.layout.shaders.forEach { shader ->
			val type = when (shader.type.lowercase()[0]) {
				'v' -> Graphics.VERTEX_SHADER
				'f' -> Graphics.FRAGMENT_SHADER
				else -> throw RuntimeException("Unsupported shader type: ${shader.type}")
			}
			val source = IO.getText(shader.file)
			shaders[type] = createShader(source, type)
		}

		resource.layout.uniforms.forEach {
			uniforms[it.name] = Graphics.getUniformLocation(program, it.name)
		}

		uniformScope = UniformScope(uniforms)

		Graphics.linkProgram(program)
		val status = Graphics.getProgrami(program, Graphics.LINK_STATUS)
		if (status == Graphics.FALSE) {
			LOGGER.error("Shader ${resource.layout.name} failed to link program: $program")
			LOGGER.error(Graphics.getProgramInfoLog(program))
			throw RuntimeException()
		}
	}
	
	fun bind() {
		Graphics.useProgram(program)
	}

	fun unbind() {
		Graphics.useProgram(0)
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
		val shader = Graphics.createShader(type)
		Graphics.shaderSource(shader, source)
		Graphics.compileShader(shader)
		val status = Graphics.getShaderi(shader, Graphics.COMPILE_STATUS)
		if (status == Graphics.FALSE) {
			LOGGER.error("Shader ${resource.layout.name} failed to compile shader: $shader")
			LOGGER.error(Graphics.getShaderInfoLog(shader))
			throw RuntimeException()
		}
		Graphics.attachShader(program, shader)
		return shader
	}

	fun dispose() {
		Graphics.deleteProgram(program)
		shaders.values.forEach {
			Graphics.deleteShader(it)
		}
	}
}