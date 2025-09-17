package cc.lapiz.solstice.core.rendering.pipeline.vertex

sealed interface Vertex {
	val x: Float
	val y: Float
}

data class Vertex_P(override val x: Float, override val y: Float) : Vertex
data class Vertex_PC(override val x: Float, override val y: Float, val r: Float, val g: Float, val b: Float, val a: Float) : Vertex