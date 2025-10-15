package cc.lapiz.solstice.game.level.grid.pathfinding

import cc.lapiz.solstice.game.level.grid.*
import org.joml.*
import java.util.*
import kotlin.math.*

class GridPathfinder(private val grid: Grid) {
	fun findPath(
		startX: Int,
		startY: Int,
		endX: Int,
		endY: Int,
		allowDiagonal: Boolean = true,
	): List<Vector2i> {
		if (!grid.isValidPosition(startX, startY) || !grid.isValidPosition(endX, endY)) {
			return emptyList()
		}

		if (!isWalkable(endX, endY)) {
			return emptyList()
		}

		val openSet = PriorityQueue<PathNode>(compareBy { it.fCost })
		val closedSet = mutableSetOf<PathNode>()
		val allNodes = mutableMapOf<Pair<Int, Int>, PathNode>()

		val startNode = PathNode(startX, startY, gCost = 0f, hCost = heuristic(startX, startY, endX, endY))
		allNodes[startX to startY] = startNode
		openSet.add(startNode)

		while (openSet.isNotEmpty()) {
			val current = openSet.poll()

			if (current.x == endX && current.y == endY) {
				return reconstructPath(current)
			}

			closedSet.add(current)

			val neighbors = getNeighbors(current.x, current.y, allowDiagonal)

			for ((nx, ny) in neighbors) {
				if (!isWalkable(nx, ny)) continue

				val neighborNode = allNodes.getOrPut(nx to ny) {
					PathNode(nx, ny, hCost = heuristic(nx, ny, endX, endY))
				}

				if (neighborNode in closedSet) continue

				val isDiagonal = nx != current.x && ny != current.y
				val moveCost = if (isDiagonal) 1.414f else 1f
				val tentativeGCost = current.gCost + moveCost

				if (tentativeGCost < neighborNode.gCost) {
					neighborNode.parent = current
					neighborNode.gCost = tentativeGCost

					if (neighborNode !in openSet) {
						openSet.add(neighborNode)
					}
				}
			}
		}

		return emptyList()
	}

	private fun isWalkable(x: Int, y: Int): Boolean {
		if (!grid.isValidPosition(x, y)) return false
		return grid.getCellAt(x, y).gridStructure == null
	}

	private fun getNeighbors(x: Int, y: Int, allowDiagonal: Boolean): List<Pair<Int, Int>> {
		val neighbors = mutableListOf<Pair<Int, Int>>()

		neighbors.add(x to y + 1)
		neighbors.add(x to y - 1)
		neighbors.add(x - 1 to y)
		neighbors.add(x + 1 to y)

		if (allowDiagonal) {
			neighbors.add(x - 1 to y + 1)
			neighbors.add(x + 1 to y + 1)
			neighbors.add(x - 1 to y - 1)
			neighbors.add(x + 1 to y - 1)
		}

		return neighbors.filter { (nx, ny) -> grid.isValidPosition(nx, ny) }
	}

	private fun heuristic(x1: Int, y1: Int, x2: Int, y2: Int): Float {
		val dx = abs(x2 - x1)
		val dy = abs(y2 - y1)

		return sqrt((dx * dx + dy * dy).toFloat())
	}

	private fun reconstructPath(endNode: PathNode): List<Vector2i> {
		val path = mutableListOf<Vector2i>()
		var current: PathNode? = endNode

		while (current != null) {
			path.add(Vector2i(current.x, current.y))
			current = current.parent
		}

		return path.reversed()
	}
}