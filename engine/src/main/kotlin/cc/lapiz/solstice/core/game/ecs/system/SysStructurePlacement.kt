package cc.lapiz.solstice.game.ecs.system

import cc.lapiz.solstice.data.*
import cc.lapiz.solstice.event.*
import cc.lapiz.solstice.game.ecs.component.impl.*
import cc.lapiz.solstice.game.ecs.component.impl.generic.Children
import cc.lapiz.solstice.game.ecs.component.impl.structure.StructureCursor
import cc.lapiz.solstice.game.ecs.component.impl.structure.StructurePreview
import cc.lapiz.solstice.game.ecs.entity.*
import cc.lapiz.solstice.game.level.grid.structure.*
import cc.lapiz.solstice.input.*
import cc.lapiz.solstice.rendering.*
import kotlin.math.*

class SysStructurePlacement : System {
	override fun update(sys: ECS, delta: Float) {
		for (result in sys.query(Transform::class, StructureCursor::class, Children::class)) {
			val transform = result.require<Transform>()
			val cursor = result.require<StructureCursor>()

			val mp = Input.getMousePosition()
			val mouseWorld = RenderSystem.Camera.screenToWorld(mp.x, mp.y)

			val gx = mouseWorld.x / cursor.grid.cellSize
			val gy = mouseWorld.y / cursor.grid.cellSize

			val originX = (gx - (cursor.structure?.width ?: 1) / 2f).roundToInt()
			val originY = (gy - (cursor.structure?.height ?: 1) / 2f).roundToInt()

			val centerX = (originX + (cursor.structure?.width ?: 1) / 2f) * cursor.grid.cellSize
			val centerY = (originY + (cursor.structure?.height ?: 1) / 2f) * cursor.grid.cellSize

			val isValidPlacement = if (cursor.structure != null) {
				val gridStructure = GridStructure(originX, originY, cursor.structure.id)
				cursor.grid.canPlace(gridStructure)
			} else {
				true
			}

			sys.query(StructurePreview::class, Transform::class, SpriteRenderer::class).first().apply {
				require<StructurePreview>().structure = cursor.structure
				val spriteRenderer = require<SpriteRenderer>()
				spriteRenderer.sprite = cursor.structure?.sprite

				if (isValidPlacement) {
					spriteRenderer.color.set(1f, 1f, 1f, 0.4f)
				} else {
					spriteRenderer.color.set(1f, 0.3f, 0.3f, 0.4f)
				}

				require<Transform>().apply {
					position.set(centerX, centerY)
					z = 0.1f
					scale.set(
						(cursor.structure?.width ?: 1) * cursor.grid.cellSize / 2f,
						(cursor.structure?.height ?: 1) * cursor.grid.cellSize / 2f
					)
				}
			}

			val gridCoords = cursor.grid.worldToGrid(mouseWorld.x, mouseWorld.y)
			val worldCenter = cursor.grid.gridToWorldCenter(gridCoords.x, gridCoords.y)
			transform.position.set(worldCenter)
		}
	}

	override fun onEvent(sys: ECS, event: Event) {
		if (event is InputEvent.MousePress && event.button == MouseButton.LEFT) {
			val entitiesToCreate = mutableListOf<() -> Unit>()

			for (result in sys.query(StructureCursor::class)) {
				val cursor = result.require<StructureCursor>()
				if (cursor.structure != null) {
					val mp = Input.getMousePosition()
					val mouseWorld = RenderSystem.Camera.screenToWorld(mp.x, mp.y)

					val gx = mouseWorld.x / cursor.grid.cellSize
					val gy = mouseWorld.y / cursor.grid.cellSize

					val originX = (gx - cursor.structure.width / 2f).roundToInt()
					val originY = (gy - cursor.structure.height / 2f).roundToInt()

					val gridStructure = GridStructure(originX, originY, cursor.structure.id)

					if (cursor.grid.placeStructure(gridStructure)) {
						val centerX = (originX + cursor.structure.width / 2f) * cursor.grid.cellSize
						val centerY = (originY + cursor.structure.height / 2f) * cursor.grid.cellSize

						entitiesToCreate.add {
							sys.createEntity("structure_${cursor.structure.name.lowercase()}",
								Transform().apply {
									position.set(centerX, centerY)
									scale.set(
										cursor.structure.width * cursor.grid.cellSize / 2f,
										cursor.structure.height * cursor.grid.cellSize / 2f
									)
								},
								SpriteRenderer(cursor.structure.toSprite()),
								gridStructure,
							)
						}
					}
				}
			}

			entitiesToCreate.forEach { it() }
		}
	}
}