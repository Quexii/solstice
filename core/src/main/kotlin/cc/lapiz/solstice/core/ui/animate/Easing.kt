package cc.lapiz.solstice.core.ui.animate

import kotlin.math.pow

object Easing {
	val Linear: (Float) -> Float = { it }
	val EaseOut: (Float) -> Float = { 1f - (1f - it).pow(2f) }
	val EaseIn: (Float) -> Float = { it.pow(2f) }
	val EaseInOut: (Float) -> Float = { t ->
		if (t < 0.5f) 2f * t * t
		else 1f - (-2f * t + 2f).pow(2f) / 2f
	}
}