package cc.lapiz.solstice.core.game.ecs.entity

import cc.lapiz.solstice.core.game.ecs.component.*
import kotlin.reflect.*

class ECS {
	private val archetypes = mutableListOf<Archetype>()
	private var nextId = 0

	fun createEntity(vararg comps: Any): Entity {
		val entity = EntityFactory.create(nextId++, 0)
		var sig = Signature()
		val compMap = mutableMapOf<Int, Any>()
		for (c in comps) {
			val id = ComponentRegistry.id(c::class)
			compMap[id] = c
			sig = sig.with(c::class)
		}

		val arch = archetypes.find { it.signature == sig } ?: Archetype(sig).also { archetypes.add(it) }
		arch.add(entity, compMap)

		return entity
	}

	fun query(vararg compTypes: KClass<*>): Sequence<Triple<Entity, List<Any>, Archetype>> {
		var sig = Signature()
		for (t in compTypes) sig = sig.with(t)

		return archetypes.asSequence().filter { it.signature.containsAll(sig) }.flatMap { arch ->
				arch.allEntities().map { e ->
					val comps = compTypes.map { t ->
						arch.getColumn(t)!![arch.allEntities().indexOf(e)]
					}
					Triple(e, comps, arch)
				}
			}
	}
}