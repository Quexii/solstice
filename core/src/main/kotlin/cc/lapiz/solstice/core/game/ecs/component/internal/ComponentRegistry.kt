package cc.lapiz.solstice.core.game.ecs.component.internal

import kotlin.reflect.KClass

object ComponentRegistry {
	private val types = mutableMapOf<KClass<*>, Int>()
	fun <T: Any> id(type: KClass<T>): Int {
		return types.getOrPut(type) { types.size }
	}

	inline fun <reified T: Any> id(): Int = id(T::class)
}