package cc.lapiz.solstice.core.ui

import cc.lapiz.solstice.core.ui.style.Alignment
import cc.lapiz.solstice.core.ui.style.Arrangement
import cc.lapiz.solstice.core.ui.style.Padding
import cc.lapiz.solstice.core.ui.style.Modifier
import cc.lapiz.solstice.core.ui.widget.Widget

fun Root(modifier: Modifier = Modifier, alignment: Alignment = Alignment.TopStart, arrangement: Arrangement = Arrangement.Vertical, init: Widget.() -> Unit): Widget = object : Widget(modifier, alignment, arrangement, Padding()) {
	init {
		init()
	}
}

fun Widget.Box(modifier: Modifier = Modifier, alignment: Alignment = Alignment.TopStart, arrangement: Arrangement = Arrangement.Vertical, padding: Padding = Padding(), init: Widget.() -> Unit = {}) {
	val box = object : Widget(modifier, alignment, arrangement, padding) {
		init {
			init()
		}
	}
	addChild(box)
	markDirty()
}