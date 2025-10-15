package cc.lapiz.solstice.game

import cc.lapiz.solstice.core.game.components.RequireComponent
import cc.lapiz.solstice.event.Event
import cc.lapiz.solstice.game.components.Component
import cc.lapiz.solstice.utils.getAnnotations
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.fetchAndIncrement
import kotlin.reflect.KClass

@OptIn(ExperimentalAtomicApi::class)
class GameObject {
    companion object {
        private val counter = AtomicInt(0)
    }

    val children: MutableList<GameObject> = mutableListOf()
    val components: MutableMap<KClass<out Component>, Component> = mutableMapOf()
    var name = "Game Object ${counter.fetchAndIncrement()}"

    fun onStart() {}

    fun onUpdate(delta: Float) {
        components.values.iterator().forEachRemaining { it.onUpdate(delta) }
        children.iterator().forEachRemaining { it.onUpdate(delta) }
    }

    fun onRender() {
        components.values.iterator().forEachRemaining { it.onRender() }
        children.iterator().forEachRemaining { it.onRender() }
    }

    fun onEvent(event: Event) {
        components.values.iterator().forEachRemaining { it.onEvent(event) }
        children.iterator().forEachRemaining { it.onEvent(event) }
    }

    inline fun <reified T : Component> addComponent(): T {
        val component = T::class.java.constructors[0].newInstance() as T

        component.gameObject = this
        getAnnotations(component::class).forEach { annotation ->
            when (annotation) {
                is RequireComponent -> {
                    annotation.components.forEach {
                        if (!components.containsKey(it)) error("Component requirements not met: ${it.simpleName}")
                    }
                }
            }
        }
        components[T::class] = component
        return component
    }

    inline fun <reified T : Component> getComponent(): T? {
        return components.getOrDefault(T::class, null) as T?
    }
}