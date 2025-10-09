package cc.lapiz.solstice.core.data

import kotlinx.serialization.Serializable

@Serializable
data class Vector2(var x: Float = 0f, var y: Float = 0f) {
	constructor(x: Int, y: Int) : this(x.toFloat(), y.toFloat())
	constructor(x: Double, y: Double) : this(x.toFloat(), y.toFloat())

	fun set(x: Float, y: Float) {
		this.x = x
		this.y = y
	}

	fun set(other: Vector2) {
		this.x = other.x
		this.y = other.y
	}

	operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)
	operator fun plusAssign(other: Vector2) {
		x += other.x
		y += other.y
	}
	operator fun minus(other: Vector2) = Vector2(x - other.x, y - other.y)
	operator fun times(scalar: Float) = Vector2(x * scalar, y * scalar)
	operator fun div(scalar: Float) = Vector2(x / scalar, y / scalar)

	fun cpy() = Vector2(x, y)

	fun length() = kotlin.math.sqrt(x * x + y * y)
	fun normalize(): Vector2 {
		val len = length()
		return if (len != 0f) Vector2(x / len, y / len) else Vector2(0f, 0f)
	}
	fun distanceSquared(other: Vector2) = (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y)
	fun distance(other: Vector2) = kotlin.math.sqrt(distanceSquared(other))

	override fun toString() = "Vector2($x, $y)"
}
