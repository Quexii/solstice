package cc.lapiz.solstice.core.ui.style

import org.lwjgl.nanovg.NVGColor

fun Attrib.fill(color: NVGColor) = then(FillAttrib(color))
fun Attrib.stroke(color: NVGColor, strokeWidth: Float = 1f) = then(StrokeAttrib(color, strokeWidth))
fun Attrib.size(width: Float? = null, height: Float? = null) = then(SizeAttrib(width, height))
fun Attrib.fullSize(widthRatio: Float? = null, heightRatio: Float? = null) = then(FullSizeAttrib(widthRatio, heightRatio))