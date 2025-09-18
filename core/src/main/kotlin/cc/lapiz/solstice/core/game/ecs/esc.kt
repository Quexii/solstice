package cc.lapiz.solstice.core.game.ecs

typealias Entity = Int

interface System {
	fun update(ECS: ECS, dt: Float)
}