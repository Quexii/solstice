package cc.lapiz.solstice.core.rendering.pipeline.vertex

import org.lwjgl.opengl.*

enum class VertexAttributeType(val componentCount: Int, val sizeInBytes: Int) {
	FLOAT(1, Float.SIZE_BYTES),
	VEC2(2, Float.SIZE_BYTES * 2),
	VEC3(3, Float.SIZE_BYTES * 3),
	VEC4(4, Float.SIZE_BYTES * 4),
	INT(1, Int.SIZE_BYTES),
	IVEC2(2, Int.SIZE_BYTES * 2),
	IVEC3(3, Int.SIZE_BYTES * 3),
	IVEC4(4, Int.SIZE_BYTES * 4);

	fun get(): Int = when (this) {
		FLOAT, VEC2, VEC3, VEC4 -> GL33C.GL_FLOAT
		INT, IVEC2, IVEC3, IVEC4 -> GL33C.GL_INT
	}
}
