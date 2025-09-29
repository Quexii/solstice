package cc.lapiz.solstice.core.game.level.grid.structure

import cc.lapiz.solstice.core.game.ecs.component.impl.*
import cc.lapiz.solstice.core.resource.*
import cc.lapiz.solstice.core.resource.impl.*

object Structures {
	val Base = Structure("Base", 0, 2, 2, sprite("structure_base"))

	private fun sprite(id: String): SpriteResource = ResourceManager.get(id) ?: error("SpriteResource '$id' not found")
}