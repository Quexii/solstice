package cc.lapiz.solstice.core.ui.style

import cc.lapiz.solstice.core.font.FontFace
import cc.lapiz.solstice.core.rendering.RenderSystem
import cc.lapiz.solstice.core.rendering.nanovg.TextAlign
import cc.lapiz.solstice.core.ui.Colors
import cc.lapiz.solstice.core.ui.layout.Direction
import cc.lapiz.solstice.core.ui.layout.Flex
import org.lwjgl.nanovg.NVGColor

open class Style {
	data class Rect(
		val fill: NVGColor? = null,
		val stroke: NVGColor? = null,
		val strokeWidth: Float = 0f
	) : Style()

	data class Text(
		val fontSize: Float = 16f,
		val textAlign: TextAlign = TextAlign.LeftTop,
		val fontColor: NVGColor = Colors.TextPrimary,
		val fontFace: FontFace = RenderSystem.DefaultFont.Default,
	) : Style()

	data class Stack(
		val spacing: Float = 0f,
		val direction: Direction = Direction.Column,
		val align: cc.lapiz.solstice.core.ui.layout.Align = cc.lapiz.solstice.core.ui.layout.Align.Start,
		val flex: Flex = Flex.Start,
	) : Style()

	object Empty : Style()
}