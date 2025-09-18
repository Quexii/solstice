package cc.lapiz.solstice.core.utils

import org.joml.Matrix4f
import org.slf4j.*
import java.nio.*

fun logger(name: String) = LoggerFactory.getLogger(name)
fun logger(clazz: Class<*>) = LoggerFactory.getLogger(clazz)

object Buffers {
	fun createFloatBuffer(size: Int) = ByteBuffer
		.allocateDirect(size * java.lang.Float.BYTES)
		.order(ByteOrder.nativeOrder())
		.asFloatBuffer()

	fun createIntBuffer(size: Int) = ByteBuffer
		.allocateDirect(size * Integer.BYTES)
		.order(ByteOrder.nativeOrder())
		.asIntBuffer()

	fun createByteBuffer(size: Int) = ByteBuffer
		.allocateDirect(size)
		.order(ByteOrder.nativeOrder())
}

fun Matrix4f.toString(): String {
	val sb = StringBuilder()
	for (i in 0 until 4) {
		sb.append("[")
		for (j in 0 until 4) {
			sb.append(String.format("%8.3f", get(j, i)))
			if (j < 3) sb.append(", ")
		}
		sb.append("]\n")
	}
	return sb.toString()
}