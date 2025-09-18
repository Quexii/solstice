package cc.lapiz.solstice.core

import cc.lapiz.solstice.core.event.*
import cc.lapiz.solstice.core.game.SceneManager
import cc.lapiz.solstice.core.game.scenes.main.MainScene
import cc.lapiz.solstice.core.rendering.*
import cc.lapiz.solstice.core.rendering.nanovg.NVcanvas
import cc.lapiz.solstice.core.time.*
import cc.lapiz.solstice.core.utils.*
import cc.lapiz.solstice.core.window.*

open class Platform {
	protected val LOGGER = logger(this::class.java)

	lateinit var timer: Timer
		protected set

	open fun start() {
		RenderSystem.init()
		SceneManager.setScene(MainScene())
	}

	protected fun update(delta: Float) {
		SceneManager.update(delta)
	}

	protected fun event(event: Event) {
		if (event is WindowEvent) Window.handleEvents(event)
		SceneManager.handleEvents(event)
	}

	protected fun render() {
		RenderSystem.Camera.projectWorld()
		SceneManager.render()
		NVcanvas.beginFrame(Window.width().toFloat(), Window.height().toFloat(), 1f)
		SceneManager.ui()
		NVcanvas.endFrame()
	}
}