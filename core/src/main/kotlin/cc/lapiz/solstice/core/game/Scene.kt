package cc.lapiz.solstice.core.game

import cc.lapiz.solstice.core.dev.Editor
import cc.lapiz.solstice.core.event.*
import cc.lapiz.solstice.core.game.ecs.entity.*
import cc.lapiz.solstice.core.rendering.RenderSystem
import cc.lapiz.solstice.core.ui.immediate.SIUI
import cc.lapiz.solstice.core.utils.Props

abstract class Scene {
	val ecs = ECS()

	open fun onEnter() {}

	open fun onExit() {}

	open fun update(delta: Float) {
		ecs.updateSystems(delta)
	}

	open fun render() {
		RenderSystem.setShader { SpriteShader }
		ecs.renderSystems()
	}

	open fun onEvent(event: Event) {
		if (Props.EDITOR) {
			return
		}

		ecs.onEvent(event)
	}

	open fun resize(width: Int, height: Int) {

	}

	open fun nanovg() {

	}
}