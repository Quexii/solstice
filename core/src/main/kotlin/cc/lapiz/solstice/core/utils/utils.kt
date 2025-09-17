package cc.lapiz.solstice.core.utils

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