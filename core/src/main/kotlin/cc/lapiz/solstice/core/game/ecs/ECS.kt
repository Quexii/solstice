package cc.lapiz.solstice.core.game.ecs

class ECS {
    private var nextEntity = 0
    private val entities = mutableSetOf<Entity>()
    private val stores = mutableMapOf<Class<*>, ComponentStore<*>>()
    private val systems = mutableListOf<System>()

    fun createEntity(): Entity {
        val e = nextEntity++
        entities += e
        return e
    }

    fun destroyEntity(e: Entity) {
        entities.remove(e)
        stores.values.forEach { (it as ComponentStore<*>).remove(e) }
    }

    fun <T : Any> registerComponent(type: Class<T>) {
        stores[type] = ComponentStore<T>()
    }

    fun <T : Any> addComponent(e: Entity, comp: T) {
        (stores[comp::class.java] as? ComponentStore<T>)?.set(e, comp)
            ?: error("Component ${comp::class.java.simpleName} not registered")
    }

    fun <T : Any> getComponent(e: Entity, type: Class<T>): T? =
        (stores[type] as? ComponentStore<T>)?.get(e)

    fun addSystem(system: System) {
        systems += system
    }

    fun update(dt: Float) {
        systems.forEach { it.update(this, dt) }
    }

	fun render() {
		systems.forEach { it.render(this) }
	}

    fun allEntities(): Set<Entity> = entities

	fun clear() {
		entities.clear()
		stores.clear()
		systems.clear()
		nextEntity = 0
	}
}
