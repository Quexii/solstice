package cc.lapiz.solstice.core.data

import cc.lapiz.solstice.core.utils.clamp1
import kotlinx.serialization.Serializable
import org.joml.Vector4f

@Serializable
data class Color(var r: Float, var g: Float, var b: Float, var a: Float = 1f) {
	init {
		r = clamp1(r)
		g = clamp1(g)
		b = clamp1(b)
		a = clamp1(a)
	}

	private var backing: Vector4f
		get() = Vector4f(r, g, b, a)
		set(value) {
			r = clamp1(value.x)
			g = clamp1(value.y)
			b = clamp1(value.z)
			a = clamp1(value.w)
		}

	constructor(v: Vector4f) : this(v.x, v.y, v.z, v.w)
	constructor(all: Float, alpha: Float = 1f) : this(all, all, all, alpha)

	fun toVector4f() = backing

	fun set(r: Float, g: Float, b: Float, a: Float = 1f) {
		this.r = clamp1(r)
		this.g = clamp1(g)
		this.b = clamp1(b)
		this.a = clamp1(a)
	}
}