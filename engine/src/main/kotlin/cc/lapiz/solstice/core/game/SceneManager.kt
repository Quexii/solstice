package cc.lapiz.solstice.game

import cc.lapiz.solstice.event.*

object SceneManager {
	private lateinit var current: Scene
	fun setScene(scene: Scene) {
		if (::current.isInitialized) current.onExit()
		current = scene
		current.onEnter()
	}

	fun handleEvents(event: Event) {
		onEvent(event)
		if (event is WindowEvent.Resize) current.resize(event.width, event.height)
	}

	private fun onEvent(event: Event) = current.onEvent(event)
	fun update(dt: Float) = current.update(dt)
	fun render() = current.render()
	fun ui() = current.nanovg()
	fun getCurrent(): Scene = current
}
