package cc.lapiz.solstice.core.game.ecs.entity

import cc.lapiz.solstice.core.event.Event
import cc.lapiz.solstice.core.game.ecs.component.impl.Name
import cc.lapiz.solstice.core.game.ecs.component.internal.Archetype
import cc.lapiz.solstice.core.game.ecs.component.internal.ComponentRegistry
import cc.lapiz.solstice.core.game.ecs.component.internal.Signature
import cc.lapiz.solstice.core.game.ecs.system.*
import kotlin.reflect.*

class ECS {
	private val archetypes = mutableListOf<Archetype>()
	private var nextId = 0
	private val systems = mutableListOf<System>()

	fun createEntity(name: String, vararg comps: Any): Entity {
		val entity = EntityFactory.create(nextId++, 0)
		var sig = Signature()
		val compMap = mutableMapOf<Int, Any>()

		compMap[ComponentRegistry.id(Name::class)] = Name(name)
		sig = sig.with(Name::class)

		for (c in comps) {
			val id = ComponentRegistry.id(c::class)
			compMap[id] = c
			sig = sig.with(c::class)
		}

		println("Creating entity $entity:$name with signature $sig")

		val arch = archetypes.find { it.signature == sig } ?: Archetype(sig).also { archetypes.add(it) }
		arch.add(entity, compMap)

		return entity
	}

	fun addSystem(system: System) {
		systems.add(system)
	}

	fun updateSystems(dt: Float) {
		for (s in systems) s.update(this, dt)
	}

	fun renderSystems() {
		for (s in systems) s.render(this)
	}

	fun onEvent(event: Event) {
		for (s in systems) s.onEvent(this, event)
	}

	fun query(vararg compTypes: KClass<*>): Sequence<Queried> {
		val sig = compTypes.fold(Signature()) { acc, t -> acc.with(t) }
		return archetypes.asSequence()
			.filter { it.signature.containsAll(sig) }
			.flatMap { arch ->
				arch.allEntities().map { e ->
					val comps = compTypes.associateWith { t ->
						arch.getColumn(t)!![arch.allEntities().indexOf(e)]
					}
					Queried(e, comps, arch)
				}
			}
	}

	fun archtypes(): List<Archetype> = archetypes.toList()
	fun systems(): List<System> = systems.toList()

	data class Queried(
		val entity: Entity,
		val components: Map<KClass<*>, Any>,
		val archetype: Archetype
	) {
		inline fun <reified T : Any> get(): T? {
			return components[T::class] as? T
		}

		inline fun <reified T : Any> require(): T {
			return components[T::class] as T
		}
	}
}