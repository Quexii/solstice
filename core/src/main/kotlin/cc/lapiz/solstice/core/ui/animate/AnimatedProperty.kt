package cc.lapiz.solstice.core.ui.animate

import cc.lapiz.solstice.core.rendering.nanovg.*
import cc.lapiz.solstice.core.ui.*
import org.lwjgl.nanovg.*
import kotlin.properties.*
import kotlin.reflect.*

class AnimatedProperty<T>(
	element: UIElement,
	key: String,
	private val falseValue: T,
	private val trueValue: T,
	private val duration: Float = 0.3f,
	private val easing: (Float) -> Float = { it },
	private val lerp: (T, T, Float) -> T,
) : ReadOnlyProperty<Any?, T> {

	private var animationState by element.rememberState("${key}_state", false)
	private var progress by element.rememberState("${key}_progress", 0f)
	private var targetProgress by element.rememberState("${key}_target", 0f)

	val value: T
		get() = getValue(null, ::value)

	override fun getValue(thisRef: Any?, property: KProperty<*>): T {
		return lerp(falseValue, trueValue, easing(progress))
	}

	fun setState(newState: Boolean) {
		if (animationState != newState) {
			animationState = newState
			targetProgress = if (newState) 1f else 0f
		}
	}

	fun update(delta: Float) {
		if (progress != targetProgress) {
			val speed = 1f / duration
			progress = if (targetProgress > progress) {
				minOf(targetProgress, progress + speed * delta)
			} else {
				maxOf(targetProgress, progress - speed * delta)
			}
		}
	}
}

fun lerpFloat(from: Float, to: Float, t: Float): Float = from + (to - from) * t
fun lerpColor(from: NVGColor, to: NVGColor, t: Float, dst: NVGColor): NVGColor {
	return NVcanvas.nvLerpRGBA(
		from, to, t, dst
	)
}
