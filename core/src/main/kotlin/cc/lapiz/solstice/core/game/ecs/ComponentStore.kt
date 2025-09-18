package cc.lapiz.solstice.core.game.ecs

class ComponentStore<T : Any> {
    private val components = mutableMapOf<Entity, T>()

    operator fun get(entity: Entity): T? = components[entity]
    operator fun set(entity: Entity, component: T) { components[entity] = component }
    fun remove(entity: Entity) { components.remove(entity) }
    fun has(entity: Entity): Boolean = entity in components
    fun all(): Iterable<Map.Entry<Entity, T>> = components.entries
}
