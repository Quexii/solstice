package cc.lapiz.solstice.game.level.grid.structure

import cc.lapiz.solstice.game.ecs.component.impl.structure.Structure
import kotlinx.serialization.Serializable

@Serializable
class GridStructure(val x: Int, val y: Int, val structureId: Int) {
	val structure: Structure
		get() = Structures.getById(structureId) ?: error("Structure '$structureId' not found")
}