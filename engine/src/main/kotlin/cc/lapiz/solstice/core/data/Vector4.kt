package cc.lapiz.solstice.core.data

import kotlinx.serialization.Serializable

@Serializable
data class Vector4(var x: Float = 0f, var y: Float = 0f, var z: Float = 0f, var w: Float = 0f) {
	constructor(x: Int, y: Int, z: Int, w: Int) : this(x.toFloat(), y.toFloat(), z.toFloat(), w.toFloat())
	constructor(x: Double, y: Double, z: Double, w: Double) : this(x.toFloat(), y.toFloat(), z.toFloat(), w.toFloat())

	fun set(x: Float, y: Float, z: Float, w: Float) {
		this.x = x
		this.y = y
		this.z = z
		this.w = w
	}

	fun set(other: Vector4) {
		this.x = other.x
		this.y = other.y
		this.z = other.z
		this.w = other.w
	}

	operator fun plus(other: Vector4) = Vector4(x + other.x, y + other.y, z + other.z, w + other.w)
	operator fun minus(other: Vector4) = Vector4(x - other.x, y - other.y, z - other.z, w - other.w)
	operator fun times(scalar: Float) = Vector4(x * scalar, y * scalar, z * scalar, w * scalar)
	operator fun div(scalar: Float) = Vector4(x / scalar, y / scalar, z / scalar, w / scalar)

	fun length() = kotlin.math.sqrt(x * x + y * y + z * z + w * w)
	fun normalize(): Vector4 {
		val len = length()
		return if (len != 0f) Vector4(x / len, y / len, z / len, w / len) else Vector4(0f, 0f, 0f, 0f)
	}
	fun distanceSquared(other: Vector4) = (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) + (z - other.z) * (z - other.z) + (w - other.w) * (w - other.w)
	fun distance(other: Vector4) = kotlin.math.sqrt(distanceSquared(other))

	override fun toString() = "Vector4($x, $y, $z, $w)"
}
