package cc.lapiz.solstice.core.resource.impl

import cc.lapiz.solstice.core.serialization.ShaderLayout
import cc.lapiz.solstice.core.resource.*
import kotlinx.serialization.json.Json

class ShaderResource(override val id: String, override val name: String, override val path: String) : Resource, Loadable {
	lateinit var layout: ShaderLayout

	override fun load() {
		val text = IO.getText(path)
		layout = Json.decodeFromString<ShaderLayout>(text)
	}
}