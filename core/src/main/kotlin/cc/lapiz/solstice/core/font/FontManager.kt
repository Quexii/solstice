package cc.lapiz.solstice.core.font

import cc.lapiz.solstice.core.resource.ResourceManager
import cc.lapiz.solstice.core.resource.impl.FontResource

object FontManager {
	private val fonts = mutableMapOf<String, FontFamily>()

	val Default: FontFamily
		get() = fonts["font_default"] ?: throw IllegalStateException("Default font not loaded!")


	fun loadFonts() {
		fonts.clear()
		loadFont(ResourceManager.load(FontResource("font_default", "Default Font", "links/font_default.json")) as FontResource)
	}

	fun loadFont(res: FontResource) {
		fonts[res.id] = FontFamily(res.layout)
	}

	fun getFont(id: String): FontFamily? = fonts[id]
}