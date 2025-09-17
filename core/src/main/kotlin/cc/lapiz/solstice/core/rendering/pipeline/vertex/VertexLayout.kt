package cc.lapiz.solstice.core.rendering.pipeline.vertex

class VertexLayout {
    private val attributes = mutableListOf<VertexAttribute>()
    
    fun addAttribute(index: Int, type: VertexAttributeType, normalized: Boolean = false, divisor: Int = 0): VertexLayout {
        attributes.add(VertexAttribute(index, type, normalized, divisor))
        return this
    }
    
    fun getStride(): Int = attributes.sumOf { it.type.sizeInBytes }
    fun getAttributes(): List<VertexAttribute> = attributes.toList()
}