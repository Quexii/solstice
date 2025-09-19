package cc.lapiz.solstice.core.ui.style

import cc.lapiz.solstice.core.font.FontFace
import cc.lapiz.solstice.core.rendering.RenderSystem
import cc.lapiz.solstice.core.rendering.nanovg.Align
import cc.lapiz.solstice.core.ui.Colors
import org.lwjgl.nanovg.NVGColor

interface Modifier {
	fun then(other: Modifier): Modifier = CombinedModifier(this, other)

	companion object : Modifier {
		override fun then(other: Modifier): Modifier = other
	}
}

data class CombinedModifier(val first: Modifier, val second: Modifier) : Modifier {
	override fun then(other: Modifier): Modifier = CombinedModifier(this, other)
}

// Style
class FillModifier(private val color: NVGColor) : Modifier {
	fun getColor(): NVGColor = color
}

class StrokeModifier(private val color: NVGColor, private val width: Float) : Modifier {
	fun getColor(): NVGColor = color
	fun getWidth(): Float = width
}

class TextModifier(private val text: String, private val fontSize: Float, private val color: NVGColor, private val face: FontFace, private val textAlign: Align) : Modifier {
	fun getText(): String = text
	fun getFontSize(): Float = fontSize
	fun getColor(): NVGColor = color
	fun getFace(): FontFace = face
	fun getTextAlign(): Align = textAlign
}

// Size
class SizeModifier(private val width: Float, private val height: Float) : Modifier {
	fun getWidth(): Float = width
	fun getHeight(): Float = height
}

class WidthModifier(private val width: Float) : Modifier {
	fun getWidth(): Float = width
}

class HeightModifier(private val height: Float) : Modifier {
	fun getHeight(): Float = height
}

class MaxSizeModifier() : Modifier
class MaxWidthModifier() : Modifier
class MaxHeightModifier() : Modifier

// Padding
class PaddingModifier(private val padding: Padding) : Modifier {
	fun getPadding(): Padding = padding
}

// Behavior
class ClickableModifier(val onClick: () -> Unit) : Modifier

fun Modifier.fill(color: NVGColor): Modifier = this.then(FillModifier(color))
fun Modifier.stroke(color: NVGColor, width: Float): Modifier = this.then(StrokeModifier(color, width))
fun Modifier.text(text: String, fontSize: Float = 20f, color: NVGColor = Colors.TextPrimary, face: FontFace = RenderSystem.DefaultFont.Default, textAlign: Align = Align.LeftTop): Modifier = this.then(TextModifier(text, fontSize, color, face, textAlign))
fun Modifier.size(width: Float, height: Float): Modifier = this.then(SizeModifier(width, height))
fun Modifier.size(size: Float): Modifier = this.then(SizeModifier(size, size))
fun Modifier.width(width: Float): Modifier = this.then(WidthModifier(width))
fun Modifier.height(height: Float): Modifier = this.then(HeightModifier(height))
fun Modifier.maxSize(): Modifier = this.then(MaxSizeModifier())
fun Modifier.maxWidth(): Modifier = this.then(MaxWidthModifier())
fun Modifier.maxHeight(): Modifier = this.then(MaxHeightModifier())
fun Modifier.padding(padding: Padding): Modifier = this.then(PaddingModifier(padding))
fun Modifier.padding(all: Float): Modifier = this.then(PaddingModifier(Padding(all)))
fun Modifier.padding(horizontal: Float, vertical: Float): Modifier = this.then(PaddingModifier(Padding(horizontal, vertical)))
fun Modifier.padding(left: Float, top: Float, right: Float, bottom: Float): Modifier = this.then(PaddingModifier(Padding(left, top, right, bottom)))
fun Modifier.clickable(onClick: () -> Unit): Modifier = this.then(ClickableModifier(onClick))