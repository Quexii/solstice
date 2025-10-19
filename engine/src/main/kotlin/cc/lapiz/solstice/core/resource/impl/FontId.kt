package cc.lapiz.solstice.core.resource.impl

import cc.lapiz.solstice.core.resource.IO
import cc.lapiz.solstice.core.resource.Loadable
import cc.lapiz.solstice.core.resource.Id
import cc.lapiz.solstice.core.serialization.FontJSON
import kotlinx.serialization.json.Json

class FontId(override val id: String, override val path: String): Id, Loadable {
	lateinit var layout: FontJSON

	override fun load() {
		val text = IO.getText(path)
		layout = Json.decodeFromString<FontJSON>(text)
	}
}