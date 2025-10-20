package cc.lapiz.solstice.core.rendering.pipeline.shader

import org.lwjgl.opengl.GL33C

data class ShaderData(val name: String, val data: String, val type: ShaderType, val uniforms: List<Uniform>, val attributes: List<Attribute>)

enum class ShaderType(val glType: Int) {
    Vertex(GL33C.GL_VERTEX_SHADER), Fragment(GL33C.GL_FRAGMENT_SHADER)
}