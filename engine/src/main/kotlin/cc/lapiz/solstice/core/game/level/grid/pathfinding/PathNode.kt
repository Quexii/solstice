package cc.lapiz.solstice.game.level.grid.pathfinding

class PathNode(
    val x: Int,
    val y: Int,
    var gCost: Float = Float.MAX_VALUE, // Cost from start
    var hCost: Float = 0f, // Heuristic cost to end
    var parent: PathNode? = null
) {
    val fCost: Float get() = gCost + hCost
    
    override fun equals(other: Any?): Boolean {
        if (other !is PathNode) return false
        return x == other.x && y == other.y
    }
    
    override fun hashCode(): Int = x * 31 + y
}