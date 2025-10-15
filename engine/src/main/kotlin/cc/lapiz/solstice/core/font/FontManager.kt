package cc.lapiz.solstice.font

import cc.lapiz.solstice.resource.ResourceManager
import cc.lapiz.solstice.resource.impl.FontId

object FontManager {
	private val fonts = mutableMapOf<String, FontFamily>()

	val Default: FontFamily
		get() = fonts["font_default"] ?: throw IllegalStateException("Default font not loaded!")


	fun loadFonts() {
		fonts.clear()
		loadFont(ResourceManager.load(FontId("font_default", "links/font_default.json")) as FontId)
	}

	fun loadFont(res: FontId) {
		fonts[res.id] = FontFamily(res.layout)
	}

	fun getFont(id: String): FontFamily? = fonts[id]
}