package cc.lapiz.solstice.core

import cc.lapiz.solstice.core.event.*
import cc.lapiz.solstice.core.game.SceneManager
import cc.lapiz.solstice.core.game.scenes.game.GameScene
import cc.lapiz.solstice.core.game.scenes.main.MainScene
import cc.lapiz.solstice.core.input.Input
import cc.lapiz.solstice.core.input.Keys
import cc.lapiz.solstice.core.rendering.*
import cc.lapiz.solstice.core.rendering.nanovg.NVcanvas
import cc.lapiz.solstice.core.rendering.platform.Graphics
import cc.lapiz.solstice.core.resource.ResourceManager
import cc.lapiz.solstice.core.resource.impl.SpriteResource
import cc.lapiz.solstice.core.time.*
import cc.lapiz.solstice.core.ui.Colors
import cc.lapiz.solstice.core.utils.*
import cc.lapiz.solstice.core.window.*

open class Platform {
	protected val LOGGER = logger(this::class.java)

	lateinit var timer: Timer
		protected set

	open fun start() {
		RenderSystem.init()

		ResourceManager.load(SpriteResource("base_unit", "Base Unit", "sprites/base_unit.png"))

		SceneManager.setScene(MainScene())
	}

	protected fun update(delta: Float) {
		RenderSystem.Camera.projectWorld()
		SceneManager.update(delta)

		Props.DEBUG = Input.isKeyPressed(Keys.F3)
	}

	protected fun event(event: Event) {
		if (event is WindowEvent) Window.handleEvents(event)
		SceneManager.handleEvents(event)
	}

	protected fun render() {
		RenderSystem.clear(0.1f, 0.1f, 0.1f, 1f)
		RenderSystem.enableAlpha()
		RenderSystem.Camera.projectWorld()
		SceneManager.render()
		NVcanvas.beginFrame(Window.width().toFloat(), Window.height().toFloat(), 1f)
		SceneManager.ui()
		if (Props.DEBUG) NVcanvas.text(4f, 4f, "FPS: ${(1f / timer.deltaTime).toInt()}", Colors.TextPrimary, 12f)
		NVcanvas.endFrame()
	}
}