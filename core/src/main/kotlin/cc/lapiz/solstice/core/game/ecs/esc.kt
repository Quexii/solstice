package cc.lapiz.solstice.core.game.ecs

typealias Entity = Int

interface System {
	fun update(ecs: ECS, dt: Float) {}
	fun render(ecs: ECS) {}
}