package cc.lapiz.solstice.core.ui.style

import cc.lapiz.solstice.core.ui.Px
import cc.lapiz.solstice.core.ui.animate.AnimatedProperty
import cc.lapiz.solstice.core.ui.px
import org.lwjgl.nanovg.NVGColor

fun Attrib.fill(color: NVGColor) = then(FillAttrib(color))
fun Attrib.fill(color: AnimatedProperty<NVGColor>) = then(FillAttrib(color))
fun Attrib.stroke(color: NVGColor, strokeWidth: Px = 1.px) = then(StrokeAttrib(color, strokeWidth))
fun Attrib.stroke(color: AnimatedProperty<NVGColor>, strokeWidth: Px = 1.px) = then(StrokeAttrib(color, strokeWidth))
fun Attrib.size(width: Px? = null, height: Px? = null) = then(SizeAttrib(width, height))
fun Attrib.fullSize(widthRatio: Px? = null, heightRatio: Px? = null) = then(FullSizeAttrib(widthRatio, heightRatio))