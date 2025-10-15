package cc.lapiz.solstice.data

import cc.lapiz.solstice.core.ui.Colors
import cc.lapiz.solstice.rendering.nanovg.NVcanvas
import cc.lapiz.solstice.utils.clamp1
import imgui.ImVec4
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

	constructor(v: Vector4f) : this(v.x, v.y, v.z, v.w)
	constructor(all: Float, alpha: Float = 1f) : this(all, all, all, alpha)

	fun lighten(amount: Float): Color {
		val clampedAmount = amount.coerceIn(0f, 1f)
		return Color(
			r + (1f - r) * clampedAmount,
			g + (1f - g) * clampedAmount,
			b + (1f - b) * clampedAmount,
			a
		)
	}

	fun darken(amount: Float): Color {
		val clampedAmount = amount.coerceIn(0f, 1f)
		return Color(
			r * (1f - clampedAmount),
			g * (1f - clampedAmount),
			b * (1f - clampedAmount),
			a
		)
	}

	fun withAlpha(alpha: Float) = this.copy(a = alpha)

	fun toNanoVG() = NVcanvas.RGBAf(r, g, b, a)
	fun toInt() =
		((a * 255).toInt() shl 24) or ((r * 255).toInt() shl 16) or ((g * 255).toInt() shl 8) or (b * 255).toInt()

	fun toImVec4() = ImVec4(r,g,b,a)

	fun set(r: Float, g: Float, b: Float, a: Float = 1f) {
		this.r = clamp1(r)
		this.g = clamp1(g)
		this.b = clamp1(b)
		this.a = clamp1(a)
	}

	companion object {
		fun HEX(argb: Long): Color {
			val r = ((argb shr 16) and 0xFF) / 255f
			val g = ((argb shr 8) and 0xFF) / 255f
			val b = (argb and 0xFF) / 255f
			val a = ((argb shr 24) and 0xFF) / 255f
			return Color(r, g, b, a)
		}

//		fun HEX(argb: Long) = HEX(argb.toInt())
	}
}