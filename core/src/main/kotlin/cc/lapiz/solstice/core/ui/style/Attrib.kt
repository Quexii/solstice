package cc.lapiz.solstice.core.ui.style

import cc.lapiz.solstice.core.font.FontFace
import cc.lapiz.solstice.core.rendering.nanovg.*
import cc.lapiz.solstice.core.ui.*
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

class FillAttrib(val color: NVGColor) : DrawAttrib() {
	override fun apply(element: UIElement) {
		val (x, y, w, h) = element.bounds
		NVcanvas.rect(x, y, w, h, color)
	}
}

class StrokeAttrib(val color: NVGColor, val strokeWidth: Float = 1f) : DrawAttrib() {
	override fun apply(element: UIElement) {
		val (x, y, w, h) = element.bounds
		NVcanvas.strokeRect(x, y, w, h, color, strokeWidth)
	}
}

class SizeAttrib(val width: Float? = null, val height: Float? = null) : LayoutAttrib() {
	override fun apply(element: UIElement) {
		element.properties.size = element.properties.size.copy(
			width = width ?: element.properties.size.width, height = height ?: element.properties.size.height
		)
	}
}

class FullSizeAttrib(val widthRatio: Float? = null, val heightRatio: Float? = null) : LayoutAttrib() {
	override fun apply(element: UIElement) {
		val parent = element.parent ?: return

		element.properties.size = element.properties.size.copy(
			width = widthRatio?.let { parent.properties.size.width * it } ?: element.properties.size.width,
			height = heightRatio?.let { parent.properties.size.height * it } ?: element.properties.size.height
		)
	}
}