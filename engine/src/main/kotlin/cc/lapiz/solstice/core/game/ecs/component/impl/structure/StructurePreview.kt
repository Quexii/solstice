package cc.lapiz.solstice.game.ecs.component.impl.structure

import cc.lapiz.solstice.game.level.grid.structure.Structures
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class StructurePreview(var structureId: Int = -1) {
	init {
		Structures.getById(structureId)?.let {
			structure = it
		}
	}

	@Transient
	var structure: Structure? = null
		set(value) {
			field = value
			structureId = value?.id ?: -1
		}
}