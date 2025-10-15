package cc.lapiz.solstice.game.level.grid

import cc.lapiz.solstice.data.Vector2
import cc.lapiz.solstice.game.level.grid.structure.GridStructure
import org.joml.Vector2f
import org.joml.Vector2i
import kotlin.math.floor

class GridCell(val x: Int, val y: Int, var gridStructure: GridStructure? = null)

class Grid(val width: Int, val height: Int, val cellSize: Float) {
	private val halfWidth = width / 2
	private val halfHeight = height / 2
	private val cells = Array(width) { x -> Array(height) { y -> GridCell(x - halfWidth, y - halfHeight) } }

	fun isValidPosition(x: Int, y: Int): Boolean {
		return x in -halfWidth until halfWidth && y in -halfHeight until halfHeight
	}

	fun getCellAt(x: Int, y: Int): GridCell {
		if (!isValidPosition(x, y)) error("Invalid grid position: ($x, $y)")
		return cells[x + halfWidth][y + halfHeight]
	}

	fun getStructureAt(x: Int, y: Int): GridStructure? {
		return if (isValidPosition(x, y)) getCellAt(x, y).gridStructure else null
	}

	fun canPlace(gridStructure: GridStructure): Boolean {
		if (!isValidPosition(gridStructure.x, gridStructure.y) ||
		    !isValidPosition(gridStructure.x + gridStructure.structure.width - 1, gridStructure.y + gridStructure.structure.height - 1)) {
			return false
		}

		for (x in gridStructure.x until gridStructure.x + gridStructure.structure.width) {
			for (y in gridStructure.y until gridStructure.y + gridStructure.structure.height) {
				if (getCellAt(x, y).gridStructure != null) {
					return false
				}
			}
		}

		return true
	}

	fun placeStructure(gridStructure: GridStructure): Boolean {
		if (!canPlace(gridStructure)) return false

		for (x in gridStructure.x until gridStructure.x + gridStructure.structure.width) {
			for (y in gridStructure.y until gridStructure.y + gridStructure.structure.height) {
				getCellAt(x, y).gridStructure = gridStructure
			}
		}

		return true
	}

	fun removeStructure(gridStructure: GridStructure) {
		for (x in gridStructure.x until gridStructure.x + gridStructure.structure.width) {
			for (y in gridStructure.y until gridStructure.y + gridStructure.structure.height) {
				if (isValidPosition(x, y) && getCellAt(x, y).gridStructure == gridStructure) {
					getCellAt(x, y).gridStructure = null
				}
			}
		}
	}

	fun worldToGrid(wx: Float, wy: Float): Vector2i {
		val gx = floor(wx / cellSize).toInt()
		val gy = floor(wy / cellSize).toInt()
		return Vector2i(gx, gy)
	}

	fun gridToWorldCenter(gx: Int, gy: Int): Vector2 {
		return Vector2(
			gx * cellSize + cellSize / 2f,
			gy * cellSize + cellSize / 2f
		)
	}
}
