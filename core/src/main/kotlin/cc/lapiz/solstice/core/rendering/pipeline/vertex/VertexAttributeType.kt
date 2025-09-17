package cc.lapiz.solstice.core.rendering.pipeline.vertex

import cc.lapiz.solstice.core.rendering.platform.Gr
import cc.lapiz.solstice.core.rendering.platform.Types

enum class VertexAttributeType(val componentCount: Int, val sizeInBytes: Int) {
	FLOAT(1, 4),
	VEC2(2, 8),
	VEC3(3, 12),
	VEC4(4, 16),
	INT(1, 4),
	IVEC2(2, 8),
	IVEC3(3, 12),
	IVEC4(4, 16);

	fun get(): Int = when (this) {
		FLOAT, VEC2, VEC3, VEC4 -> Gr.Types.FLOAT
		INT, IVEC2, IVEC3, IVEC4 -> Gr.Types.INT
	}
}
