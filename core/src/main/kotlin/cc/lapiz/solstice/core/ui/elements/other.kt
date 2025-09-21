package cc.lapiz.solstice.core.ui.elements

import cc.lapiz.solstice.core.ui.*
import cc.lapiz.solstice.core.ui.action.*
import cc.lapiz.solstice.core.ui.layout.*
import cc.lapiz.solstice.core.ui.style.*

fun UIElement.center(attrib: Attrib, block: UIStack.() -> Unit) = stack(attrib, flex = Flex.Center, align = Align.Center, init = block)

fun UIElement.button(text: String, onClick: () -> Unit) = center(
	Attrib.size(200f, 50f).fill(Colors.Panel).stroke(Colors.Border, 2f)
) {
	label(Attrib, text)
}.onClick { onClick() }