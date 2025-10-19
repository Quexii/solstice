package cc.lapiz.solstice.core.rendering.nanovg

import org.lwjgl.nanovg.NanoVG

enum class TextAlign(val value: Int) {
	LeftTop(NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_TOP),
	LeftMiddle(NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_MIDDLE),
	LeftBottom(NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_BOTTOM),
	CenterTop(NanoVG.NVG_ALIGN_CENTER or NanoVG.NVG_ALIGN_TOP),
	CenterMiddle(NanoVG.NVG_ALIGN_CENTER or NanoVG.NVG_ALIGN_MIDDLE),
	CenterBottom(NanoVG.NVG_ALIGN_CENTER or NanoVG.NVG_ALIGN_BOTTOM),
	RightTop(NanoVG.NVG_ALIGN_RIGHT or NanoVG.NVG_ALIGN_TOP),
	RightMiddle(NanoVG.NVG_ALIGN_RIGHT or NanoVG.NVG_ALIGN_MIDDLE),
	RightBottom(NanoVG.NVG_ALIGN_RIGHT or NanoVG.NVG_ALIGN_BOTTOM)
}