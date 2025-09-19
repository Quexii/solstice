package cc.lapiz.solstice.core.ui.widget

import cc.lapiz.solstice.core.rendering.nanovg.*
import cc.lapiz.solstice.core.ui.behavior.*
import cc.lapiz.solstice.core.ui.style.*
import cc.lapiz.solstice.core.window.*

fun Widget.applyFill(style: FillModifier) {
	val color = style.getColor()
	NVcanvas.rect(x, y, width, height, color)
}

fun Widget.applyStroke(style: StrokeModifier) {
	val color = style.getColor()
	val strokeWidth = style.getWidth()
	NVcanvas.strokeRect(x, y, width, height, color, strokeWidth)
}

fun Widget.applyText(style: TextModifier) {
	val text = style.getText()
	val fontSize = style.getFontSize()
	val color = style.getColor()
	val face = style.getFace()
	val textAlign = style.getTextAlign()

	val xPos = when (textAlign) {
		Align.LeftTop, Align.LeftMiddle, Align.LeftBottom -> x + padding.left
		Align.CenterTop, Align.CenterMiddle, Align.CenterBottom -> x + width / 2
		Align.RightTop, Align.RightMiddle, Align.RightBottom -> x + width - padding.right
	}

	val yPos = when (textAlign) {
		Align.LeftTop, Align.CenterTop, Align.RightTop -> y + padding.top
		Align.LeftMiddle, Align.CenterMiddle, Align.RightMiddle -> y + height / 2
		Align.LeftBottom, Align.CenterBottom, Align.RightBottom -> y + height - padding.bottom
	}

	NVcanvas.text(xPos, yPos, text, fontSize, 0f, color, face, textAlign)
}

fun Widget.applySize(style: SizeModifier) {
	width = style.getWidth()
	height = style.getHeight()
}

fun Widget.applyWidth(style: WidthModifier) {
	width = style.getWidth()
}

fun Widget.applyHeight(style: HeightModifier) {
	height = style.getHeight()
}

fun Widget.applyMaxSize() {
	if (parent == null) {
		width = Window.width().toFloat()
		height = Window.height().toFloat()
	} else {
		width = parent!!.width - padding.left - padding.right
		height = parent!!.height - padding.top - padding.bottom
	}
}

fun Widget.applyMaxWidth() {
	parent?.let {
		width = it.width - it.padding.left - it.padding.right
	}
}

fun Widget.applyMaxHeight() {
	parent?.let {
		height = it.height - it.padding.top - it.padding.bottom
	}
}

fun Widget.applyPadding(style: PaddingModifier) {
	padding = style.getPadding()
}

fun Widget.applyClickable(style: ClickableModifier) {
	behaviors += InputUIBehavior.MousePress(style.onClick)
}