package cc.lapiz.solstice.core.game.components

import cc.lapiz.solstice.core.game.components.impl.MeshRenderer
import cc.lapiz.solstice.core.game.components.impl.SpriteRenderer
import cc.lapiz.solstice.core.game.components.impl.Transform
import kotlin.reflect.KClass

object ComponentRegistry {
    private val components = mutableListOf<KClass<out Component>>()

    fun initInternal() {
        register(Transform::class)
        register(MeshRenderer::class)
        register(SpriteRenderer::class)
    }

    fun <T: Component> register(klass: KClass<T>): Boolean {
        if (components.contains(klass)) return false
        components.add(klass)
        return true
    }

    inline fun <reified T: Component> register(): Boolean = register(T::class)

    fun getAll() = components.toList()
}