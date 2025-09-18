package cc.lapiz.solstice.core.resource.impl

import cc.lapiz.solstice.core.resource.IO
import cc.lapiz.solstice.core.resource.Loadable
import cc.lapiz.solstice.core.resource.Resource
import cc.lapiz.solstice.core.serialization.FontLayout
import kotlinx.serialization.json.Json

class FontResource(override val id: String, override val name: String, override val path: String): Resource, Loadable {
	lateinit var layout: FontLayout

	override fun load() {
		val text = IO.getText(path)
		layout = Json.decodeFromString<FontLayout>(text)
	}
}