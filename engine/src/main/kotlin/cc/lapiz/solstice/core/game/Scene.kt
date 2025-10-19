package cc.lapiz.solstice.core.game

import cc.lapiz.solstice.core.event.Event

abstract class Scene {
//	val ecs = ECS()
	val gameObjects = mutableListOf<GameObject>()

	open fun onEnter() {}

	open fun onExit() {}

	open fun update(delta: Float) {
		gameObjects.iterator().forEachRemaining { gameObject ->
			gameObject.onUpdate(delta)
		}
	}

	open fun render() {
		gameObjects.iterator().forEachRemaining { gameObject ->
			gameObject.onRender()
		}
	}

	open fun onEvent(event: Event) {
		gameObjects.iterator().forEachRemaining { gameObject ->
			gameObject.onEvent(event)
		}
	}

	open fun resize(width: Int, height: Int) {}

	open fun nanovg() {}

	fun createObject(name: String? = null, config: GameObject.() -> Unit) {
		val obj = GameObject()
		name?.let { obj.name = it }
		config(obj)
		gameObjects.add(obj)
	}
}