package cc.lapiz.solstice.resource.impl

import cc.lapiz.solstice.serialization.ShaderJSON
import cc.lapiz.solstice.resource.*
import kotlinx.serialization.json.Json

class ShaderId(override val id: String, override val path: String) : Id, Loadable {
	lateinit var layout: ShaderJSON

	override fun load() {
		val text = IO.getText(path)
		layout = Json.decodeFromString<ShaderJSON>(text)
	}
}