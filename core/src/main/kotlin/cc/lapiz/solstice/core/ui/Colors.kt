package cc.lapiz.solstice.core.ui

import cc.lapiz.solstice.core.rendering.nanovg.NVcanvas

object Colors {
	val Background      = NVcanvas.HEX(0x0D1117FF)
	val Panel           = NVcanvas.HEX(0x161B22FF)
	val PanelHighlight  = NVcanvas.HEX(0x21262DFF)

	val Primary         = NVcanvas.HEX(0x1F6FEBFF)
	val PrimaryHover    = NVcanvas.HEX(0x388BFDFF)
	val PrimaryActive   = NVcanvas.HEX(0x1158C7FF)

	val Secondary       = NVcanvas.HEX(0xF78166FF)
	val SecondaryHover  = NVcanvas.HEX(0xFFA28BFF)
	val SecondaryActive = NVcanvas.HEX(0xC9510CFF)

	val TextPrimary     = NVcanvas.HEX(0xFFFFFFFF)
	val TextSecondary   = NVcanvas.HEX(0xC9D1D9FF)
	val TextDisabled    = NVcanvas.HEX(0x6E7681FF)

	val Border          = NVcanvas.HEX(0x30363DFF)
	val Shadow          = NVcanvas.HEX(0x000000AA)
}