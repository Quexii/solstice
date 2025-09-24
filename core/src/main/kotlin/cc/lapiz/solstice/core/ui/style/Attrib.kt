package cc.lapiz.solstice.core.ui.style

import cc.lapiz.solstice.core.font.FontFace
import cc.lapiz.solstice.core.rendering.nanovg.*
import cc.lapiz.solstice.core.ui.*
import cc.lapiz.solstice.core.ui.animate.AnimatedProperty
import cc.lapiz.solstice.core.ui.elements.UILabel
import org.lwjgl.nanovg.*

abstract class Attrib {
	abstract fun apply(element: UIElement)

	fun then(other: Attrib): Attrib = CombinedAttrib(this, other)

	companion object : Attrib() {
		override fun apply(element: UIElement) {}
	}
}

data class CombinedAttrib(val first: Attrib, val second: Attrib) : Attrib() {
	override fun apply(element: UIElement) {}
}


abstract class DrawAttrib : Attrib()
abstract class LayoutAttrib : Attrib()
abstract class EventAttrib : Attrib()

class FillAttrib(val color: Any) : DrawAttrib() {
	override fun apply(element: UIElement) {
		val (x, y, w, h) = element.bounds
		val color = when (color) {
			is NVGColor -> color
			is AnimatedProperty<*> -> color.value as? NVGColor ?: throw IllegalArgumentException("AnimatedProperty must be of type NVGColor")
			else -> throw IllegalArgumentException("Unsupported color type: ${color::class}")
		}
		NVcanvas.rect(x, y, w, h, color)
	}
}

class StrokeAttrib(val color: Any, val strokeWidth: Px = 1f.px) : DrawAttrib() {
	override fun apply(element: UIElement) {
		val (x, y, w, h) = element.bounds
		val color = when (color) {
			is NVGColor -> color
			is AnimatedProperty<*> -> color.value as? NVGColor ?: throw IllegalArgumentException("AnimatedProperty must be of type NVGColor")
			else -> throw IllegalArgumentException("Unsupported color type: ${color::class}")
		}
		NVcanvas.strokeRect(x, y, w, h, color, strokeWidth())
	}
}

class SizeAttrib(val width: Px? = null, val height: Px? = null) : LayoutAttrib() {
	override fun apply(element: UIElement) {
		element.properties.size = element.properties.size.copy(
			width = width?.invoke() ?: element.properties.size.width, height = height?.invoke() ?: element.properties.size.height
		)
	}
}

class FullSizeAttrib(val widthRatio: Px? = null, val heightRatio: Px? = null) : LayoutAttrib() {
	override fun apply(element: UIElement) {
		val parent = element.parent ?: return

		val newWidth = widthRatio?.let { it * parent.properties.size.width }()
		val newHeight = heightRatio?.let { it * parent.properties.size.height }()

		if (newWidth != null) element.properties.size = element.properties.size.copy(width = newWidth)
		if (newHeight != null) element.properties.size = element.properties.size.copy(height = newHeight)
	}
}