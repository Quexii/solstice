package cc.lapiz.solstice.core.game

import cc.lapiz.solstice.core.event.*
import cc.lapiz.solstice.core.game.ecs.*
import cc.lapiz.solstice.core.ui.UI
import cc.lapiz.solstice.core.ui.UIElement

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
		ecs.clear()
	}

	open fun update(delta: Float) {
		ui.update(delta)
		ecs.update(delta)
	}

	open fun render() {
		ecs.render()
	}

	open fun onEvent(evnet: Event) {
		ui.onEvent(evnet)
	}

	open fun resize(width: Int, height: Int) {
		ui.resize(width.toFloat(), height.toFloat())
	}

	open fun nanovg() {
		ui.render()
	}
}