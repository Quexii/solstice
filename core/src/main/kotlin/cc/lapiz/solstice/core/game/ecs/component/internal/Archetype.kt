package cc.lapiz.solstice.core.game.ecs.component.internal

import cc.lapiz.solstice.core.game.ecs.entity.Entity
import kotlin.collections.iterator
import kotlin.reflect.KClass

class Archetype(val signature: Signature, private val capacity: Int = 128) {
	val columns = mutableMapOf<Int, MutableList<Any>>()
	val entities = mutableListOf<Entity>()

	fun add(entity: Entity, components: Map<Int, Any>) {
		if (entities.size >= capacity) throw IllegalStateException("Archetype capacity exceeded")
		entities.add(entity)
		for ((type, component) in components) {
			val column = columns.getOrPut(type) { MutableList(capacity) { Any() } }
			column[entities.size - 1] = component
		}
	}

	fun remove(entity: Entity): Map<Int, Any>? {
		val idx = entities.indexOf(entity)
		if (idx == -1) return null
		val removed = mutableMapOf<Int, Any>()
		for ((typeId, comps) in columns) {
			removed[typeId] = comps.removeAt(idx)
		}
		entities.removeAt(idx)
		return removed
	}

	fun <T: Any> getColumn(type: KClass<T>):  List<T>? {
		return columns[ComponentRegistry.id(type)]?.map { it as T }
	}

	inline fun <reified T: Any> getColumn(): List<T>? = columns[ComponentRegistry.id<T>()]?.map { it as T }

	fun size() = entities.size
	fun allEntities() = entities.asSequence()
}