package cc.lapiz.solstice.core.font

import cc.lapiz.solstice.core.rendering.nanovg.NVcanvas
import cc.lapiz.solstice.core.resource.*
import cc.lapiz.solstice.core.serialization.*
import cc.lapiz.solstice.core.utils.logger

class FontFamily(private val layout: FontLayout) {
	val name = layout.name
	var license = layout.license
		private set
	val path = layout.path

	private val faces = mutableMapOf<String, FontFace>()

	private val LOGGER = logger(this::class.java)

	val Default get() = faces["regular"] ?: faces["default"]!!
	val Regular get() = faces["regular"] ?: Default
	val Bold get() = faces["bold"] ?: Default
	val Light get() = faces["light"] ?: Default
	val Thin get() = faces["thin"] ?: Default
	val Medium get() = faces["medium"] ?: Default
	val SemiBold get() = faces["semibold"] ?: Default
	val ExtraBold get() = faces["extrabold"] ?: Default
	val Black get() = faces["black"] ?: Default

	init {
		val path = if (path.endsWith('/')) path else "$path/"
		license = IO.getText("$path$license")

		layout.fonts.forEach {
			val data = IO.getBuffer("$path${it.file}")
			val name = name.lowercase() + "-" + it.face.lowercase()
			val weight = when (it.face.lowercase()) {
				"thin" -> FontWeight.THIN
				"light" -> FontWeight.LIGHT
				"regular" -> FontWeight.REGULAR
				"medium" -> FontWeight.MEDIUM
				"semibold", "demibold" -> FontWeight.SEMIBOLD
				"bold" -> FontWeight.BOLD
				"extrabold", "ultrabold" -> FontWeight.EXTRABOLD
				"black", "heavy" -> FontWeight.BLACK
				else -> FontWeight.DEFAULT
			}
			if (it.default == true) {
				faces["default"] = FontFace(weight, name)
				if (weight == FontWeight.DEFAULT) {
					return@forEach
				}
			}
			faces[it.face] = FontFace(weight, name)
			LOGGER.info("Loading font face: $name with weight: $weight")

			if (!NVcanvas.initFont(name, data)) {
				throw RuntimeException("Failed to load font: $name")
			}
		}

		if (faces.isEmpty()) {
			LOGGER.error("No fonts found in family: $name")
			throw RuntimeException("No fonts found in family: $name")
		}

		if (!faces.containsKey("default")) {
			faces["default"] = faces.values.first()
		}

		LOGGER.info("Loaded font family: $name with ${faces.size} faces")
	}
}