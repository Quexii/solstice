package cc.lapiz.solstice.game.level.grid.structure

import cc.lapiz.solstice.game.ecs.component.impl.structure.Structure
import cc.lapiz.solstice.resource.*
import cc.lapiz.solstice.resource.impl.*

object Structures {
	private val idMap = mutableMapOf<Int, Structure>()

	val Base = put(Structure("Base", 0, 2, 2, sprite("structure_base")))

	private fun sprite(id: String): SpriteId = ResourceManager.get(id) ?: error("SpriteResource '$id' not found")

	fun getById(id: Int): Structure? = idMap[id]

	fun put(structure: Structure): Structure {
		idMap[structure.id] = structure
		return structure
	}
}