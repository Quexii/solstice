package cc.lapiz.solstice.core.resource.impl

import cc.lapiz.solstice.core.resource.IO
import cc.lapiz.solstice.core.resource.Id
import cc.lapiz.solstice.core.resource.Loadable
import cc.lapiz.solstice.core.serialization.ShaderJSON
import kotlinx.serialization.json.Json

class ShaderId(override val id: String, override val path: String) : Id, Loadable {
	lateinit var layout: ShaderJSON

	override fun load() {
		val text = IO.getText(path)
		layout = Json.decodeFromString<ShaderJSON>(text)
	}
}