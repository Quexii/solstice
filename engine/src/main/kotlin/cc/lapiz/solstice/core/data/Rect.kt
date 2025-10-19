package cc.lapiz.solstice.core.data

import kotlinx.serialization.Serializable

@Serializable
data class Rect(
	var x: Float,
	var y: Float,
	var width: Float,
	var height: Float
) {
	val centerX get() = x + width / 2
	val centerY get() = y + height / 2

	fun set(other: Rect) {
		x = other.x
		y = other.y
		width = other.width
		height = other.height
	}
}