package cc.lapiz.solstice.core.game.ecs.entity

typealias Entity = Int

object EntityFactory {
	private const val ENTITY_MASK = 0x00FFFFFF
	private const val GEN_SHIFT = 24

	fun create(id: Int, generation: Int) = (generation shl GEN_SHIFT) or (id and ENTITY_MASK)
	fun id(entity: Entity) = entity and ENTITY_MASK
}