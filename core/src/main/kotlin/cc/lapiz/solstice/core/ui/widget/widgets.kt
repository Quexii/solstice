package cc.lapiz.solstice.core.ui.widget

import cc.lapiz.solstice.core.font.FontFace
import cc.lapiz.solstice.core.rendering.RenderSystem
import cc.lapiz.solstice.core.rendering.nanovg.Align
import cc.lapiz.solstice.core.ui.*
import cc.lapiz.solstice.core.ui.style.Alignment
import cc.lapiz.solstice.core.ui.style.Modifier
import cc.lapiz.solstice.core.ui.style.clickable
import cc.lapiz.solstice.core.ui.style.fill
import cc.lapiz.solstice.core.ui.style.maxSize
import cc.lapiz.solstice.core.ui.style.stroke
import cc.lapiz.solstice.core.ui.style.text
import org.lwjgl.nanovg.NVGColor

fun Widget.Label(
	modifier: Modifier,
	text: String,
	fontSize: Float = 20f,
	color: NVGColor = Colors.TextPrimary,
	face: FontFace = RenderSystem.DefaultFont.Default,
	textAlign: Align = Align.LeftTop
) = Box(modifier.text(text, fontSize, color, face, textAlign), alignment = Alignment.Center)

fun Widget.Button(
	modifier: Modifier,
	label: String,
	onClick: () -> Unit
) = Box(modifier.fill(Colors.Panel).stroke(Colors.Primary, 2f).clickable(onClick),
	alignment = Alignment.Center) {
	Label(Modifier.maxSize(), label, textAlign = Align.CenterMiddle)
}