package cc.lapiz.solstice.core

import cc.lapiz.solstice.core.debug.*
import cc.lapiz.solstice.core.event.*
import cc.lapiz.solstice.core.game.*
import cc.lapiz.solstice.core.game.scenes.game.*
import cc.lapiz.solstice.core.input.*
import cc.lapiz.solstice.core.rendering.*
import cc.lapiz.solstice.core.rendering.nanovg.*
import cc.lapiz.solstice.core.resource.*
import cc.lapiz.solstice.core.resource.impl.*
import cc.lapiz.solstice.core.time.*
import cc.lapiz.solstice.core.utils.*
import cc.lapiz.solstice.core.window.*

open class Platform {
	lateinit var timer: Timer
		protected set

	open fun start() {
		RenderSystem.init()

		ResourceManager.load(SpriteResource("base_unit", "Base Unit", "sprites/unit_base.png"))
		ResourceManager.load(SpriteResource("structure_base", "Base", "sprites/structure_base.png"))
		ResourceManager.load(SpriteResource("cursor_box", "Box Cursor", "sprites/cursor_box.png"))

		SceneManager.setScene(GameScene())
	}

	protected fun update(delta: Float) {
		RenderSystem.Camera.projectWorld()
		SceneManager.update(delta)

		Props.DEBUG = Input.isKeyPressed(Keys.F3)
		if (Props.DEBUG) DBG.update()
	}

	protected fun event(event: Event) {
		if (event is WindowEvent) Window.handleEvents(event)
		if (event is InputEvent.MouseScroll) {
			RenderSystem.Camera.zoomBy(event.amount)
		}
		SceneManager.handleEvents(event)
	}

	protected fun render() {
		RenderSystem.clear(0.1f, 0.1f, 0.1f, 1f)
		RenderSystem.enableAlpha()
		RenderSystem.enableDepth()
		RenderSystem.Camera.projectWorld()
		SceneManager.render()
		NVcanvas.beginFrame(Window.width().toFloat(), Window.height().toFloat(), 1f)
		SceneManager.ui()
		if (Props.DEBUG) {
			DBG.left("FPS: ${(timer.fps).toInt()}")
			DBG.renderTexts()
		}
		NVcanvas.endFrame()
	}
}