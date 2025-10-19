package cc.lapiz.solstice.core.utils

import kotlin.math.PI

fun toRadians(deg: Double): Double = deg / 180.0 * PI
fun toDegrees(rad: Double): Double = rad * 180.0 / PI

fun pointInCircle(px: Float, py: Float, cx: Float, cy: Float, radius: Float): Boolean {
	val dx = px - cx
	val dy = py - cy
	return dx * dx + dy * dy <= radius * radius
}

fun clamp(value: Float, min: Float, max: Float): Float {
	return when {
		value < min -> min
		value > max -> max
		else -> value
	}
}

fun clamp1(value: Float): Float = clamp(value, 0f, 1f)