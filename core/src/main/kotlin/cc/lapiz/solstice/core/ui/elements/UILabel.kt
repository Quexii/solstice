package cc.lapiz.solstice.core.ui.elements

import cc.lapiz.solstice.core.font.*
import cc.lapiz.solstice.core.rendering.RenderSystem
import cc.lapiz.solstice.core.rendering.nanovg.*
import cc.lapiz.solstice.core.ui.*
import cc.lapiz.solstice.core.ui.style.*
import cc.lapiz.solstice.core.utils.*
import org.lwjgl.nanovg.*
import org.lwjgl.system.*
import kotlin.properties.*

class UILabel(attrib: Attrib, initialText: String, var fontFace: FontFace, var fontSize: Float, var textColor: NVGColor) : UIElement(attrib) {
	var text by Delegates.observable(initialText) { _, _, newValue ->
		calcTextSize(newValue)
	}

	init {
		calcTextSize(initialText)
	}

	override fun draw() {
		val (x, y) = properties.position
		NVcanvas.text(x, y, text, textColor, fontSize, fontFace)

		if (Props.DEBUG) {
			NVcanvas.strokeRect(properties.position.x, properties.position.y, properties.size.width, properties.size.height, Colors.Debug.Green, 1f)
		}
	}

	private fun calcTextSize(text: String) {
		MemoryStack.stackPush().use { stack ->
			val buffer = stack.mallocFloat(4)
			NVcanvas.nvFontSize(fontSize)
			NVcanvas.nvTextFontFace(fontFace.id)
			NVcanvas.nvTextBounds(text, buffer)
			val textWidth = buffer.get(2) - buffer.get(0)
			val textHeight = buffer.get(3) - buffer.get(1)
			properties.size = properties.size.copy(width = textWidth, height = textHeight)
		}
	}
}

fun UIElement.label(attrib: Attrib, text: String, fontFace: FontFace = RenderSystem.DefaultFont.Default, fontSize: Float = 21f, textColor: NVGColor = Colors.TextPrimary): UILabel {
	val label = UILabel(attrib, text, fontFace, fontSize, textColor)
	addChild(label)
	return label
}