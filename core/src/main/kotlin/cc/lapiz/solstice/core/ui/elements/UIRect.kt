package cc.lapiz.solstice.core.ui.elements

import cc.lapiz.solstice.core.event.*
import cc.lapiz.solstice.core.rendering.nanovg.*
import cc.lapiz.solstice.core.ui.*
import cc.lapiz.solstice.core.ui.style.*
import cc.lapiz.solstice.core.utils.Props

class UIRect(attrib: Attrib) : UIElement(attrib) {
	override fun draw() {
		if (Props.DEBUG) {
			NVcanvas.strokeRect(properties.position.x, properties.position.y, properties.size.width, properties.size.height, Colors.Debug.Red, 1f)
		}
	}
}

fun UIElement.rect(attrib: Attrib = Attrib, init: UIRect.() -> Unit = {}): UIRect {
	val rect = UIRect(attrib)
	addChild(rect)
	rect.init()
	return rect
}