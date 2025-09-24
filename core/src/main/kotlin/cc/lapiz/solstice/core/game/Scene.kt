package cc.lapiz.solstice.core.game

import cc.lapiz.solstice.core.event.*
import cc.lapiz.solstice.core.game.ecs.entity.ECS
import cc.lapiz.solstice.core.ui.UI
import cc.lapiz.solstice.core.ui.UIElement
import kotlin.properties.ReadWriteProperty

abstract class Scene {
	val ecs = ECS()
	private val ui = UI()

	open fun initUI():  UIElement? {
		return null
	}

	open fun onEnter() {
		initUI()?.let { ui.setContent(it) }
	}

	open fun onExit() {
	}

	open fun update(delta: Float) {
		ui.update(delta)
	}

	open fun render() {
	}

	open fun onEvent(event: Event) {
		ui.onEvent(event)
	}

	open fun resize(width: Int, height: Int) {
		ui.resize(width.toFloat(), height.toFloat())
	}

	open fun nanovg() {
		ui.render()
	}
}