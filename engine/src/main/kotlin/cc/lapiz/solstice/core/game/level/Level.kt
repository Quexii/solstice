package cc.lapiz.solstice.game.level

import cc.lapiz.solstice.data.*
import cc.lapiz.solstice.game.ecs.component.impl.*
import cc.lapiz.solstice.game.ecs.component.impl.generic.Children
import cc.lapiz.solstice.game.ecs.component.impl.generic.Parent
import cc.lapiz.solstice.game.ecs.component.impl.structure.StructureCursor
import cc.lapiz.solstice.game.ecs.component.impl.structure.StructurePreview
import cc.lapiz.solstice.game.ecs.component.impl.ui.UICanvas
import cc.lapiz.solstice.game.ecs.entity.*
import cc.lapiz.solstice.game.ecs.system.*
import cc.lapiz.solstice.game.level.grid.*
import cc.lapiz.solstice.game.level.grid.structure.Structures
import cc.lapiz.solstice.resource.*

class Level(val name: String, val width: Int, val height: Int, val ecs: ECS) {
	val grid = Grid(width, height, 4f)

	init {
		val ghost = ecs.createEntity("placement_ghost",
			SpriteRenderer(null, Color(1f,0.4f)),
			Transform(), StructurePreview()
		)

		ecs.createEntity("placement_cursor",
			Transform().apply { z = 0.2f }, StructureCursor(grid, Structures.Base), Children(mutableListOf(ghost))
		)

		ecs.createEntity("test_unit",
			Transform().apply { scale *= 2f },
			SpriteRenderer(ResourceManager.get("base_unit")!!),
			Agent(workingGrid = grid)
		)



		val canvas = ecs.createEntity("canvas",
			Transform(), Children(), UICanvas()
		)

		ecs.addSystem(SysSpriteRenderer())
		ecs.addSystem(SysStructurePlacement())
		ecs.addSystem(SysStructure())
		ecs.addSystem(SysTestUnitCtrl())
	}

	fun update(deltaTime: Float) {

	}

	fun render() {

	}
}