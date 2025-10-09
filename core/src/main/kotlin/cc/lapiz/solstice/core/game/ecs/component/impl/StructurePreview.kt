package cc.lapiz.solstice.core.game.ecs.component.impl

import cc.lapiz.solstice.core.game.level.grid.structure.Structures
import kotlinx.serialization.Serializable

@Serializable
class StructurePreview(var structureId: Int = -1) {
	init {
		Structures.getById(structureId)?.let {
			structure = it
		}
	}

	@kotlinx.serialization.Transient
	var structure: Structure? = null
		set(value) {
			field = value
			structureId = value?.id ?: -1
		}
}