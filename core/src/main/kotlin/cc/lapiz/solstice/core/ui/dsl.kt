package cc.lapiz.solstice.core.ui

import cc.lapiz.solstice.core.ui.widget.*

fun Widget.widget(
	x: Float,
	y: Float,
	width: Float,
	height: Float,
	init: Widget.() -> Unit
): Widget {
	val widget = Widget(x, y, width, height)
	widget.init()
	this.add(widget)
	return widget
}

fun Widget.button(
	label: String,
	x: Float,
	y: Float,
	width: Float,
	height: Float,
	onClick: () -> Unit
) = this.add(Button(label, x, y, width, height, onClick))