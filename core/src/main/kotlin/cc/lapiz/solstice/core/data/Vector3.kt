package cc.lapiz.solstice.core.data

import kotlinx.serialization.Serializable

@Serializable
data class Vector3(var x: Float = 0f, var y: Float = 0f, var z: Float = 0f) {
	constructor(x: Int, y: Int, z: Int) : this(x.toFloat(), y.toFloat(), z.toFloat())
	constructor(x: Double, y: Double, z: Double) : this(x.toFloat(), y.toFloat(), z.toFloat())

	fun set(x: Float, y: Float, z: Float) {
		this.x = x
		this.y = y
		this.z = z
	}

	fun set(other: Vector3) {
		this.x = other.x
		this.y = other.y
		this.z = other.z
	}

	operator fun plus(other: Vector3) = Vector3(x + other.x, y + other.y, z + other.z)
	operator fun minus(other: Vector3) = Vector3(x - other.x, y - other.y, z - other.z)
	operator fun times(scalar: Float) = Vector3(x * scalar, y * scalar, z * scalar)
	operator fun div(scalar: Float) = Vector3(x / scalar, y / scalar, z / scalar)

	fun length() = kotlin.math.sqrt(x * x + y * y + z * z)
	fun normalize(): Vector3 {
		val len = length()
		return if (len != 0f) Vector3(x / len, y / len, z / len) else Vector3(0f, 0f, 0f)
	}
	fun distanceSquared(other: Vector3) = (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) + (z - other.z) * (z - other.z)
	fun distance(other: Vector3) = kotlin.math.sqrt(distanceSquared(other))

	override fun toString() = "Vector3($x, $y, $z)"
}