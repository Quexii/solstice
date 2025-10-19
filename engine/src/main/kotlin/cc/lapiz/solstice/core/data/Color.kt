package cc.lapiz.solstice.core.data

import cc.lapiz.solstice.rendering.nanovg.NVcanvas
import cc.lapiz.solstice.core.utils.clamp1
import imgui.ImVec4
import kotlinx.serialization.Serializable
import org.joml.Vector4f

@Serializable
data class Color(private val _r: Float, private val _g: Float, private val _b: Float, private val _a: Float = 1f) {
	val backing = floatArrayOf(_r, _g, _b, _a)
	val r: Float get() = backing[0]
	val g: Float get() = backing[1]
	val b: Float get() = backing[2]
	val a: Float get() = backing[3]

	init {
		backing[0] = clamp1(_r)
		backing[1] = clamp1(_g)
		backing[2] = clamp1(_b)
		backing[3] = clamp1(_a)
	}

	constructor(v: Vector4f) : this(v.x, v.y, v.z, v.w)
	constructor(all: Float, alpha: Float = 1f) : this(all, all, all, alpha)

	fun lighten(amount: Float): Color {
		val clampedAmount = amount.coerceIn(0f, 1f)
		return Color(
			backing[0] + (1f - backing[0]) * clampedAmount,
			backing[1] + (1f - backing[1]) * clampedAmount,
			backing[2] + (1f - backing[2]) * clampedAmount,
			backing[3]
		)
	}

	fun darken(amount: Float): Color {
		val clampedAmount = amount.coerceIn(0f, 1f)
		return Color(
			backing[0] * (1f - clampedAmount),
			backing[1] * (1f - clampedAmount),
			backing[2] * (1f - clampedAmount),
			backing[3]
		)
	}

	fun withAlpha(alpha: Float) = this.copy(_a = alpha)

	fun toNanoVG() = NVcanvas.RGBAf(backing[0], backing[1], backing[2], backing[3])
	fun toInt() =
		((backing[3] * 255).toInt() shl 24) or ((backing[0] * 255).toInt() shl 16) or ((backing[1] * 255).toInt() shl 8) or (backing[2] * 255).toInt()

	fun set(r: Float, g: Float, b: Float, a: Float = 1f) {
		backing[0] = clamp1(r)
		backing[1] = clamp1(g)
		backing[2] = clamp1(b)
		backing[3] = clamp1(a)
	}

	companion object {
		fun HEX(argb: Long): Color {
			val r = ((argb shr 16) and 0xFF) / 255f
			val g = ((argb shr 8) and 0xFF) / 255f
			val b = (argb and 0xFF) / 255f
			val a = ((argb shr 24) and 0xFF) / 255f
			return Color(r, g, b, a)
		}
	}
}