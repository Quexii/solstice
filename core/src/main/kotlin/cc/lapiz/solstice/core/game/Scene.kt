package cc.lapiz.solstice.core.game

import cc.lapiz.solstice.core.event.InputEvent
import cc.lapiz.solstice.core.game.ecs.ECS
import cc.lapiz.solstice.core.ui.UI

abstract class Scene {
	val ECS = ECS()
	val ui = UI()

	open fun onEnter() {}
	open fun onExit() {}
	open fun update(delta: Float) {}
	open fun render() {}
	open fun onInput(evnet: InputEvent) {}
	open fun resize(width: Int, height: Int) {}
	open fun ui() {}
}