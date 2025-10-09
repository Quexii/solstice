package cc.lapiz.solstice.core.game.ecs.component.internal

import kotlin.reflect.KClass

object ComponentRegistry {
	private val types = mutableMapOf<KClass<*>, Int>()
	fun <T: Any> id(type: KClass<T>): Int {
		return types.getOrPut(type) { types.size }
	}

	inline fun <reified T: Any> id(): Int = id(T::class)

	fun allRegistered(): Set<KClass<*>> = types.keys

	fun <T: Any> get(id: Int): KClass<T>? {
		return types.entries.find { it.value == id }?.key as KClass<T>?
	}

	inline fun <reified T: Any> get(): KClass<T>? = get<T>(id<T>())

	fun getRaw(id: Int): KClass<*>? {
		return types.entries.find { it.value == id }?.key
	}
}