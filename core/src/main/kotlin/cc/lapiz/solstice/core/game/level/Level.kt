package cc.lapiz.solstice.core.game.level

import cc.lapiz.solstice.core.data.*
import cc.lapiz.solstice.core.game.ecs.component.impl.*
import cc.lapiz.solstice.core.game.ecs.entity.*
import cc.lapiz.solstice.core.game.ecs.system.*
import cc.lapiz.solstice.core.game.level.grid.*
import cc.lapiz.solstice.core.game.level.grid.structure.Structures
import cc.lapiz.solstice.core.resource.*
import cc.lapiz.solstice.core.resource.impl.*
import org.joml.Vector4f

class Level(val name: String, val width: Int, val height: Int, val ecs: ECS) {
	val grid = Grid(width, height, 4f)

	init {
		val ghost = ecs.createEntity(
			SpriteRenderer(null, Vector4f(1f,1f,1f,0.4f)),
			Transform(),
			StructurePreview()
		)

		ecs.createEntity(
			SpriteRenderer(ResourceManager.get("cursor_box")!!),
			Transform().apply { z = 0.2f },
			PlacementCursor(grid, Structures.Base),
			Children(mutableListOf(ghost))
		)

		ecs.addSystem(SysSpriteRenderer())
		ecs.addSystem(SysPlacementCursor())
		ecs.addSystem(SysStructure())
	}

	fun update(deltaTime: Float) {

	}

	fun render() {

	}
}