package cc.lapiz.solstice.core.game.ecs.system

import cc.lapiz.solstice.core.event.Event
import cc.lapiz.solstice.core.game.ecs.entity.ECS

interface System {
	fun update(sys: ECS, delta: Float) {}
	fun onEvent(sys: ECS, event: Event) {}
	fun render(sys: ECS) {}
}