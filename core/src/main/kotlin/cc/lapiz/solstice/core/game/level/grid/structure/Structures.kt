package cc.lapiz.solstice.core.game.level.grid.structure

import cc.lapiz.solstice.core.game.ecs.component.impl.*
import cc.lapiz.solstice.core.resource.*
import cc.lapiz.solstice.core.resource.impl.*

object Structures {
	private val idMap = mutableMapOf<Int, Structure>()

	val Base = put(Structure("Base", 0, 2, 2, sprite("structure_base")))

	private fun sprite(id: String): SpriteResource = ResourceManager.get(id) ?: error("SpriteResource '$id' not found")

	fun getById(id: Int): Structure? = idMap[id]

	fun put(structure: Structure): Structure {
		idMap[structure.id] = structure
		return structure
	}
}