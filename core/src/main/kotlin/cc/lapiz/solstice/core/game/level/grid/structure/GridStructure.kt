package cc.lapiz.solstice.core.game.level.grid.structure

import cc.lapiz.solstice.core.game.ecs.component.impl.*
import kotlinx.serialization.Serializable

@Serializable
class GridStructure(val x: Int, val y: Int, val structureId: Int) {
	val structure: Structure
		get() = Structures.getById(structureId) ?: error("Structure '$structureId' not found")
}