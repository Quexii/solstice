package cc.lapiz.solstice.game.ecs.system

import cc.lapiz.solstice.data.*
import cc.lapiz.solstice.data.Target
import cc.lapiz.solstice.dev.F3
import cc.lapiz.solstice.event.*
import cc.lapiz.solstice.game.ecs.component.impl.*
import cc.lapiz.solstice.game.ecs.entity.*
import cc.lapiz.solstice.game.level.grid.pathfinding.*
import cc.lapiz.solstice.input.*
import cc.lapiz.solstice.rendering.*
import cc.lapiz.solstice.core.ui.Colors
import kotlin.math.atan2

class SysTestUnitCtrl : System {
	override fun update(sys: ECS, delta: Float) {
		for (queried in sys.query(Agent::class, Transform::class)) {
			val agent = queried.require<Agent>()
			val transform = queried.require<Transform>()
			val grid = agent.workingGrid
			val gridPos = grid!!.worldToGrid(transform.position.x, transform.position.y)
			if (agent.target != null && agent.target?.navigating == false && agent.target?.path == null) {
				val endPos = grid.worldToGrid(agent.target!!.x, agent.target!!.y)
				agent.target?.path = GridPathfinder(grid).findPath(gridPos.x, gridPos.y, endPos.x, endPos.y).toMutableList()
				agent.target?.navigating = true
			} else {
				if (agent.target?.navigating == true && agent.target?.path != null && agent.target!!.path!!.isNotEmpty()) {
					for ((index, i) in agent.target!!.path!!.withIndex()) {
						val world = grid.gridToWorldCenter(i.x, i.y)
						if (index > 0) {
							val prev = agent.target!!.path!![index - 1]
							val prevWorld = grid.gridToWorldCenter(prev.x, prev.y)
							F3.circleWorld(prevWorld.x, prevWorld.y, 8f, Colors.Yellow)
							F3.lineWorld(world.x, world.y, prevWorld.x, prevWorld.y, Colors.Red)
						}
					}

					val nextNode = agent.target!!.path!![0]
					val nextWorld = grid.gridToWorldCenter(nextNode.x, nextNode.y)
					F3.lineWorld(transform.position.x, transform.position.y, nextWorld.x, nextWorld.y, Colors.Cyan)
					F3.circleWorld(transform.position.x, transform.position.y, 8f, Colors.Green)
					if (nextWorld.distanceSquared(transform.position) < 8) {
						agent.target!!.path!!.removeAt(0)
						if (agent.target!!.path!!.isEmpty()) {
							agent.target = null
						}
					} else {
						val dir = (nextWorld.cpy()-transform.position).normalize()
						transform.position.plusAssign(dir * delta * 5f)
						val finalAngle = Math.toDegrees(atan2(dir.y.toDouble(), dir.x.toDouble())).toFloat()
						var angleDiff = finalAngle - transform.rotation + 90
						if (angleDiff > 180) angleDiff -= 360
						if (angleDiff < -180) angleDiff += 360
						transform.rotation += angleDiff * delta * 4f
					}
				}
			}
		}
	}

	override fun onEvent(sys: ECS, event: Event) {
		for (queried in sys.query(Agent::class)) {
			if (event is InputEvent.MousePress && event.button == MouseButton.RIGHT) {
				val mp = Input.getMousePosition()
				val wp = RenderSystem.Camera.screenToWorld(mp.x, mp.y)
				queried.require<Agent>().target = Target(-1, wp.x, wp.y, TargetType.POSITION)
			}
		}
	}
}