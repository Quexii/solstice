package cc.lapiz.solstice.ui.immediate

import cc.lapiz.solstice.data.Color
import cc.lapiz.solstice.event.*
import cc.lapiz.solstice.font.*
import cc.lapiz.solstice.input.*
import cc.lapiz.solstice.rendering.nanovg.*
import cc.lapiz.solstice.resource.impl.*

import org.joml.*

object SIUI {
	private var mousePos = Vector2f()
	private var mousePressed = false
	private var mouseReleased = false
	private var mouseDown = false
	private var hotId: Int = 0
	private var activeId: Int = 0
	private var nextId: Int = 0

	private var style = UIStyle()

	private val textInputStates = mutableMapOf<Int, TextInputState>()
	private var keyTyped: Char? = null

	private val collapsibleStates = mutableMapOf<String, Boolean>()

	private val propertyRenderers = mutableMapOf<String, (String, Any) -> Unit>()

	private var cursorX = 0f
	private var cursorY = 0f
	private var layoutSpacing = 8f
	private var layoutDirection = LayoutDirection.VERTICAL

	enum class LayoutDirection {
		VERTICAL, HORIZONTAL
	}

	data class UIStyle(
		val buttonPadding: Float = 8f,
		val buttonRounding: Float = 4f,
		val buttonColorNormal: Long = 0x4A5568FF,
		val buttonColorHover: Long = 0x5A6678FF,
		val buttonColorActive: Long = 0x3A4558FF,
		val buttonColorText: Long = 0xFFFFFFFF,
		val textColor: Long = 0xFFFFFFFF,
		val textFieldColorBg: Long = 0x2D3748FF,
		val textFieldColorBorder: Long = 0x4A5568FF,
		val textFieldColorActive: Long = 0x63B3EDFF,
		val checkboxSize: Float = 20f,
		val checkboxColorBg: Long = 0x2D3748FF,
		val checkboxColorCheck: Long = 0x48BB78FF,
		val sliderHeight: Float = 24f,
		val sliderHandleRadius: Float = 10f,
		val sliderColorTrack: Long = 0x2D3748FF,
		val sliderColorFill: Long = 0x4299E1FF,
		val sliderColorHandle: Long = 0xFFFFFFFF,
		val fontSize: Float = 16f,
		val font: FontFace = FontManager.Default.Default,
	)

	private data class TextInputState(
		var text: String = "",
		var cursorPos: Int = 0,
	)

	fun beginFrame() {
		nextId = 0
		keyTyped = null

		cursorX = layoutSpacing
		cursorY = layoutSpacing
	}

	fun endFrame() {
		if (!mouseDown) {
			hotId = 0
		}

		if (mouseReleased) {
			activeId = 0
		}

		mousePressed = false
		mouseReleased = false
	}

	fun onEvent(event: Event) {
		when (event) {
			is InputEvent.MouseMove -> {
				mousePos.set(event.position)
			}
			is InputEvent.MousePress -> {
				if (event.button == MouseButton.LEFT) {
					mousePressed = true
					mouseDown = true

					if (hotId != 0) {
						activeId = hotId
					}
				}
			}
			is InputEvent.MouseRelease -> {
				if (event.button == MouseButton.LEFT) {
					mouseReleased = true
					mouseDown = false
				}
			}
			is InputEvent.KeyType -> {
				keyTyped = event.char
			}
			else -> {}
		}
	}

	fun setLayoutDirection(direction: LayoutDirection) {
		layoutDirection = direction
	}

	fun getLayoutDirection(): LayoutDirection = layoutDirection

	fun vertical() {
		layoutDirection = LayoutDirection.VERTICAL
	}

	fun horizontal() {
		layoutDirection = LayoutDirection.HORIZONTAL
	}

	private fun advanceCursor(width: Float = 0f, height: Float = 0f) {
		when (layoutDirection) {
			LayoutDirection.VERTICAL -> {
				cursorY += height + layoutSpacing
			}
			LayoutDirection.HORIZONTAL -> {
				cursorX += width + layoutSpacing
			}
		}
	}

	fun setCursor(x: Float, y: Float) {
		cursorX = x
		cursorY = y
	}

	fun spacing(amount: Float = layoutSpacing) {
		when (layoutDirection) {
			LayoutDirection.VERTICAL -> cursorY += amount
			LayoutDirection.HORIZONTAL -> cursorX += amount
		}
	}

	fun sameLine(spacing: Float = layoutSpacing) {
		cursorY -= layoutSpacing
		cursorX += spacing
	}

	fun newLine(spacing: Float = layoutSpacing) {
		cursorX = layoutSpacing
		cursorY += spacing
	}

	fun label(text: String, align: TextAlign = TextAlign.LeftTop) {
		NVcanvas.text(cursorX, cursorY, text, Color.HEX(style.textColor), style.fontSize, style.font, align)
		val textHeight = style.fontSize
		val textWidth = NVcanvas.stringSize(text, style.fontSize, style.font)
		advanceCursor(textWidth, textHeight)
	}

	fun button(label: String, width: Float = 120f, height: Float = 30f): Boolean {
		val id = generateId()
		val x = cursorX
		val y = cursorY

		val isHovered = isMouseOver(x, y, width, height)
		val isActive = activeId == id

		if (isHovered) {
			hotId = id
			if (mousePressed) {
				activeId = id
			}
		}

		val color = when {
			isActive -> style.buttonColorActive
			isHovered -> style.buttonColorHover
			else -> style.buttonColorNormal
		}

		NVcanvas.roundedRect(x, y, width, height, style.buttonRounding, Color.HEX(color))

		NVcanvas.text(x + width / 2, y + height / 2, label, Color.HEX(style.buttonColorText), style.fontSize, style.font, TextAlign.CenterMiddle)

		advanceCursor(width, height)

		return isActive && mouseReleased && isHovered
	}

	fun imageButton(image: SpriteId, width: Float, height: Float, imageWidth: Float, imageHeight: Float): Boolean {
		val id = generateId()
		val x = cursorX
		val y = cursorY

		val isHovered = isMouseOver(x, y, width, height)
		val isActive = activeId == id

		if (isHovered) {
			hotId = id
			if (mousePressed) {
				activeId = id
			}
		}

		val color = when {
			isActive -> style.buttonColorActive
			isHovered -> style.buttonColorHover
			else -> style.buttonColorNormal
		}

		NVcanvas.roundedRect(x, y, width, height, style.buttonRounding, Color.HEX(color))

		val imgX = x + (width - imageWidth) / 2f
		val imgY = y + (height - imageHeight) / 2f
		NVcanvas.sprite(imgX, imgY, imageWidth, imageHeight, image)

		advanceCursor(width, height)

		return isActive && mouseReleased && isHovered
	}

	fun checkbox(label: String, checked: Boolean): Boolean {
		val id = generateId()
		val x = cursorX
		val y = cursorY
		val size = style.checkboxSize

		val isHovered = isMouseOver(x, y, size, size)
		var newChecked = checked

		if (isHovered) {
			hotId = id
			if (mousePressed) {
				activeId = id
				newChecked = !checked
			}
		}

		val bgColor = if (isHovered) style.buttonColorHover else style.checkboxColorBg
		NVcanvas.roundedRect(x, y, size, size, 2f, Color.HEX(bgColor))

		if (newChecked) {
			NVcanvas.nvBeginPath()
			NVcanvas.nvMoveTo(x + size * 0.2f, y + size * 0.5f)
			NVcanvas.nvLineTo(x + size * 0.4f, y + size * 0.7f)
			NVcanvas.nvLineTo(x + size * 0.8f, y + size * 0.3f)
			NVcanvas.nvStrokeColor(Color.HEX(style.checkboxColorCheck).toNanoVG())
			NVcanvas.nvStrokeWidth(2f)
			NVcanvas.nvStroke()
		}

		NVcanvas.text(x + size + 8f, y + size / 2, label, Color.HEX(style.textColor), style.fontSize, style.font, TextAlign.LeftMiddle)

		val labelWidth = NVcanvas.stringSize(label, style.fontSize, style.font)
		val totalWidth = size + 8f + labelWidth

		advanceCursor(totalWidth, size)

		return newChecked
	}

	fun slider(label: String, value: Float, min: Float, max: Float, width: Float = 200f): Float {
		val id = generateId()
		val x = cursorX
		val y = cursorY
		val height = style.sliderHeight

		NVcanvas.text(x, y + height / 2, "$label: %.2f".format(value), Color.HEX(style.textColor), style.fontSize, style.font, TextAlign.LeftMiddle)

		val sliderX = x + 120f
		val trackY = y + height / 2 - 2f
		var newValue = value

		val isHovered = isMouseOver(sliderX, y, width, height)

		if (isHovered && mousePressed) {
			activeId = id
		}

		if (activeId == id && mouseDown) {
			val t = ((mousePos.x - sliderX) / width).coerceIn(0f, 1f)
			newValue = min + t * (max - min)
		}

		NVcanvas.roundedRect(sliderX, trackY, width, 4f, 2f, Color.HEX(style.sliderColorTrack))

		val fillWidth = ((newValue - min) / (max - min)) * width
		NVcanvas.roundedRect(sliderX, trackY, fillWidth, 4f, 2f, Color.HEX(style.sliderColorFill))

		val handleX = sliderX + fillWidth
		NVcanvas.circle(handleX, y + height / 2, style.sliderHandleRadius, Color.HEX(style.sliderColorHandle))

		val totalWidth = 120f + width

		advanceCursor(totalWidth, height)

		return newValue
	}

	fun textField(label: String, text: String, width: Float = 200f, height: Float = 30f): String {
		val id = generateId()
		val x = cursorX
		val y = cursorY

		val state = textInputStates.getOrPut(id) { TextInputState(text, text.length) }

		val isHovered = isMouseOver(x, y, width, height)
		val isActive = activeId == id

		if (isHovered && mousePressed) {
			hotId = id
			activeId = id
		}

		if (isActive && keyTyped != null) {
			val char = keyTyped!!
			when {
				char == '\b' && state.text.isNotEmpty() -> {
					state.text = state.text.dropLast(1)
					state.cursorPos = state.text.length
				}
				char.isLetterOrDigit() || char.isWhitespace() || char in "!@#$%^&*()_+-=[]{}|;:',.<>?/~`" -> {
					state.text += char
					state.cursorPos = state.text.length
				}
			}
		}

		val bgColor = if (isActive) style.textFieldColorActive else style.textFieldColorBorder
		NVcanvas.roundedRect(x, y, width, height, 4f, Color.HEX(style.textFieldColorBg))
		NVcanvas.nvBeginPath()
		NVcanvas.nvRoundedRect(x, y, width, height, 4f)
		NVcanvas.nvStrokeColor(Color.HEX(bgColor).toNanoVG())
		NVcanvas.nvStrokeWidth(2f)
		NVcanvas.nvStroke()

		NVcanvas.text(x + 8f, y + height / 2, state.text.ifEmpty { label }, Color.HEX(style.textColor), style.fontSize, style.font, TextAlign.LeftMiddle)

		advanceCursor(width, height)

		return state.text
	}

	fun image(image: SpriteId, width: Float, height: Float) {
		val x = cursorX
		val y = cursorY

		NVcanvas.sprite(x, y, width, height, image)

		advanceCursor(width, height)
	}

	fun collapsingHeader(label: String, id: String = label): Boolean {
		val headerId = generateId()
		val x = cursorX
		val y = cursorY
		val height = 24f
		val width = 200f

		val isExpanded = collapsibleStates.getOrPut(id) { false }

		val isHovered = isMouseOver(x, y, width, height)

		if (isHovered) {
			hotId = headerId
			if (mousePressed) {
				activeId = headerId
				collapsibleStates[id] = !isExpanded
			}
		}

		val bgColor = if (isHovered) style.buttonColorHover else style.checkboxColorBg
		NVcanvas.roundedRect(x, y, width, height, 3f, Color.HEX(bgColor))

		val arrowX = x + 8f
		val arrowY = y + height / 2
		val arrowSize = 6f

		NVcanvas.nvBeginPath()
		if (isExpanded) {
			NVcanvas.nvMoveTo(arrowX - arrowSize / 2, arrowY - arrowSize / 4)
			NVcanvas.nvLineTo(arrowX + arrowSize / 2, arrowY - arrowSize / 4)
			NVcanvas.nvLineTo(arrowX, arrowY + arrowSize / 2)
		} else {
			NVcanvas.nvMoveTo(arrowX - arrowSize / 4, arrowY - arrowSize / 2)
			NVcanvas.nvLineTo(arrowX + arrowSize / 2, arrowY)
			NVcanvas.nvLineTo(arrowX - arrowSize / 4, arrowY + arrowSize / 2)
		}
		NVcanvas.nvClosePath()
		NVcanvas.nvFillColor(Color.HEX(style.textColor).toNanoVG())
		NVcanvas.nvFill()

		NVcanvas.text(x + 20f, y + height / 2, label, Color.HEX(style.textColor), style.fontSize, style.font, TextAlign.LeftMiddle)

		advanceCursor(width, height)

		return collapsibleStates[id] ?: false
	}

	fun indent(amount: Float = 16f) {
		cursorX += amount
	}

	fun unindent(amount: Float = 16f) {
		cursorX -= amount
	}

	fun beginWindow(title: String, x: Float, y: Float, width: Float, height: Float, header: Boolean = true) {
		NVcanvas.roundedRect(x, y, width, height, 8f, Color.HEX(0x1A202CFF))

		if (header) {
			NVcanvas.nvBeginPath()
			NVcanvas.nvRoundedRectVarying(x, y, width, 30f, 8f, 8f, 0f, 0f)
			NVcanvas.nvFillColor(Color.HEX(0x2D3748FF).toNanoVG())
			NVcanvas.nvFill()

			NVcanvas.text(x + 12f, y + 15f, title, Color.HEX(style.textColor), style.fontSize, style.font, TextAlign.LeftMiddle)
		}

		if (header) setCursor(x + 12f, y + 40f)
		else setCursor(x + 12f, y + 12f)
	}

	fun endWindow() {
		cursorX = layoutSpacing
		cursorY = layoutSpacing
	}

	fun registerPropertyRenderer(typeName: String, renderer: (name: String, value: Any) -> Unit) {
		propertyRenderers[typeName] = renderer
	}

	fun propertyValue(name: String, value: Any) {
		val typeName = value::class.simpleName ?: value::class.toString()
		val renderer = propertyRenderers[typeName]

		if (renderer != null) {
			renderer(name, value)
		} else {
			label("$name: $value")
		}
	}

	fun labeledRow(label: String, vararg values: Pair<String, Any>) {
		val startX = cursorX

		NVcanvas.text(cursorX, cursorY, label, Color.HEX(style.textColor), style.fontSize, style.font, TextAlign.LeftTop)

		val textWidth = NVcanvas.stringSize(label, style.fontSize, style.font)

		cursorX += textWidth + 20f

		for ((subLabel, value) in values) {
			NVcanvas.text(cursorX, cursorY, "$subLabel:", Color.HEX(0xA0AEC0FF), style.fontSize - 2f, style.font, TextAlign.LeftTop)

			cursorX += 15f

			val valueText = when (value) {
				is Float -> "%.3f".format(value)
				is Double -> "%.3f".format(value)
				else -> value.toString()
			}
			NVcanvas.text(cursorX, cursorY, valueText, Color.HEX(style.textColor), style.fontSize, style.font, TextAlign.LeftTop)
			val valueTextWidth = NVcanvas.stringSize(valueText, style.fontSize, style.font)

			cursorX += valueTextWidth + 20f
		}

		cursorX = startX
		cursorY += style.fontSize + layoutSpacing
	}

	private fun generateId(): Int {
		return ++nextId
	}

	private fun isMouseOver(x: Float, y: Float, width: Float, height: Float): Boolean {
		return mousePos.x >= x && mousePos.x <= x + width && mousePos.y >= y && mousePos.y <= y + height
	}

	fun getStyle(): UIStyle = style

	fun setStyle(newStyle: UIStyle) {
		style = newStyle
	}
}

