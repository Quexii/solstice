package cc.lapiz.solstice.rendering.pipeline.vertex

class VertexLayout {
    private val attributes = mutableListOf<VertexAttribute>()
    
    fun addAttribute(index: Int, type: VertexAttributeType, normalized: Boolean = false, divisor: Int = 0): VertexLayout {
        attributes.add(VertexAttribute(index, type, normalized, divisor))
        return this
    }
    
    fun getStride(): Int = attributes.sumOf { it.type.sizeInBytes }
    fun getAttributes(): List<VertexAttribute> = attributes.toList()

	companion object {
		private fun create(vararg types: VertexAttributeType): VertexLayout {
			val layout = VertexLayout()
			types.forEachIndexed { index, type ->
				layout.addAttribute(index, type)
			}
			return layout
		}

		val PosTex = create(VertexAttributeType.VEC2, VertexAttributeType.VEC2)
		val PosColor = create(VertexAttributeType.VEC2, VertexAttributeType.VEC4)
		val PosTexColor = create(VertexAttributeType.VEC2, VertexAttributeType.VEC2, VertexAttributeType.VEC4)
	}
}