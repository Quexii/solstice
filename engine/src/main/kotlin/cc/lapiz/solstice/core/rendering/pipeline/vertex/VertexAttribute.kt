package cc.lapiz.solstice.core.rendering.pipeline.vertex

import cc.lapiz.solstice.core.rendering.pipeline.vertex.VertexAttributeType

data class VertexAttribute(
    val index: Int,
    val type: VertexAttributeType,
    val normalized: Boolean = false,
    val divisor: Int = 0 // 0 = per vertex, 1+ = per instance
)