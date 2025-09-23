package cc.lapiz.solstice.core.game.ecs

class EntityQuery(private val entityId: Int, private val ecs: ECS) {
    operator fun <T : Any> get(componentClass: Class<T>): T {
        return ecs.getComponent(entityId, componentClass)
            ?: throw IllegalStateException("Entity $entityId does not have component ${componentClass.simpleName}")
    }

    fun <T : Any> getOrNull(componentClass: Class<T>): T? {
        return ecs.getComponent(entityId, componentClass)
    }

    val id: Int get() = entityId
}
