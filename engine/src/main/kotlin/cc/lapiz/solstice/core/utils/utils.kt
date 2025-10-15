package cc.lapiz.solstice.utils

import org.slf4j.*
import java.nio.ByteBuffer
import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

fun logger(name: String) = LoggerFactory.getLogger(name)
fun logger(clazz: Class<*>) = LoggerFactory.getLogger(clazz)

fun contains(x: Float, y: Float, rectX: Float, rectY: Float, rectWidth: Float, rectHeight: Float): Boolean {
	return x >= rectX && x <= rectX + rectWidth && y >= rectY && y <= rectY + rectHeight
}

fun getAnnotations(klass: KClass<*>): List<Annotation> {
	val list = mutableListOf<Annotation>()
	list.addAll(klass.annotations)
	klass.superclasses.forEach {
		list.addAll(getAnnotations(it))
	}
	return list
}

fun Int.argbToAbgr(): Int {
    val a = (this shr 24) and 0xFF
    val r = (this shr 16) and 0xFF
    val g = (this shr 8) and 0xFF
    val b = this and 0xFF
    return (a shl 24) or (b shl 16) or (g shl 8) or r
}

fun ByteBuffer.toByteArray(): ByteArray {
    val originalPosition = position()
    rewind()
    val byteArray = ByteArray(remaining())
    get(byteArray)
    position(originalPosition)
    return byteArray
}