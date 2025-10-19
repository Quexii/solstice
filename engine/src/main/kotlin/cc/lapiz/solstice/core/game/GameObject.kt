package cc.lapiz.solstice.core.game

import cc.lapiz.solstice.core.game.components.meta.RequiredComponents
import cc.lapiz.solstice.core.event.Event
import cc.lapiz.solstice.core.game.components.Component
import cc.lapiz.solstice.core.game.components.meta.JsonSerializable
import cc.lapiz.solstice.core.utils.getAnnotations
import kotlinx.serialization.json.*
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.fetchAndIncrement
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

@OptIn(ExperimentalAtomicApi::class)
class GameObject {
    companion object {
        private val counter = AtomicInt(0)
    }

    val uid: String = Random.nextLong().toString(16)
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

    fun <T : Component> addComponent(component: T): T {
        component.gameObject = this
        getAnnotations(component::class).forEach { annotation ->
            when (annotation) {
                is RequiredComponents -> {
                    annotation.components.forEach {
                        if (!components.containsKey(it)) error("Component requirements not met: ${it.simpleName}")
                    }
                }
            }
        }
        components[component::class] = component
        return component
    }

    inline fun <reified T : Component> addComponent(): T {
        val component = T::class.createInstance()
        return addComponent(component)
    }

    inline fun <reified T : Component> getComponent(): T? {
        return components.getOrDefault(T::class, null) as T?
    }

    fun getJson(): JsonObject {
        return buildJsonObject {
            put("uid", uid)
            put("name", name)

            put("components", buildJsonArray {
                components.values.forEach { comp ->
                    if (comp is JsonSerializable)
                        add(comp.toJson())
                    else
                        add(buildJsonObject {
                            put("type", comp.name)
                            put("error", "Component is not serializable")
                        })
                }
            })

            put("children", buildJsonArray {
                children.forEach { add(it.uid) }
            })
        }
    }
}