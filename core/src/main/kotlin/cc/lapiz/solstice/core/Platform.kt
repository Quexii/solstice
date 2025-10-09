package cc.lapiz.solstice.core

import cc.lapiz.solstice.core.dev.*
import cc.lapiz.solstice.core.event.*
import cc.lapiz.solstice.core.game.*
import cc.lapiz.solstice.core.game.scenes.game.*
import cc.lapiz.solstice.core.input.*
import cc.lapiz.solstice.core.rendering.*
import cc.lapiz.solstice.core.rendering.nanovg.*
import cc.lapiz.solstice.core.resource.*
import cc.lapiz.solstice.core.resource.impl.*
import cc.lapiz.solstice.core.time.*
import cc.lapiz.solstice.core.ui.immediate.SIUI
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
		//Editor Resourced
		ResourceManager.load(SpriteResource($$"$scene", "Editor Icon: Scene", "sprites/editor_icons/scene.png"))
		ResourceManager.load(SpriteResource($$"$prefabs", "Editor Icon: Prefabs", "sprites/editor_icons/prefabs.png"))
		ResourceManager.load(SpriteResource($$"$resources", "Editor Icon: Resource", "sprites/editor_icons/resources.png"))
		ResourceManager.load(SpriteResource($$"$image", "Editor Icon: Image", "sprites/editor_icons/image.png"))
		ResourceManager.load(SpriteResource($$"$file", "Editor Icon: File", "sprites/editor_icons/file.png"))
		ResourceManager.load(SpriteResource($$"$shader", "Editor Icon: Shader", "sprites/editor_icons/shader.png"))
		ResourceManager.load(SpriteResource($$"$font", "Editor Icon: Font", "sprites/editor_icons/font.png"))

		SceneManager.setScene(GameScene())
	}

	protected fun update(delta: Float) {
		RenderSystem.Camera.projectWorld()
		SceneManager.update(delta)

		if (Props.DEBUG) F3.update()
		if (Props.EDITOR) Editor.update(delta)
	}

	protected fun event(event: Event) {
		if (event is WindowEvent) Window.handleEvents(event)
		if (event is InputEvent) {
			if (event is InputEvent.MouseScroll) RenderSystem.Camera.zoomBy(event.amount)
			if (event is InputEvent.KeyPress) {
				if (event.key == Keys.F3) Props.DEBUG = !Props.DEBUG
				if (event.key == Keys.F1) Props.EDITOR = !Props.EDITOR
			}
		}

		SceneManager.handleEvents(event)
		SIUI.onEvent(event)
	}

	protected fun render() {
		RenderSystem.clear(0.1f, 0.1f, 0.1f, 1f)
		RenderSystem.enableAlpha()
		RenderSystem.enableDepth()
		RenderSystem.Camera.projectWorld()
		SceneManager.render()
		if (Props.EDITOR) Editor.render()
		NVcanvas.beginFrame(Window.width().toFloat(), Window.height().toFloat(), 1f)
		SceneManager.ui()
		if (Props.EDITOR) Editor.nanovg()
		if (Props.DEBUG) {
			F3.left("FPS: ${(timer.fps)}")
			F3.render()
		}
		NVcanvas.endFrame()
	}
}