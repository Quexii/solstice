package cc.lapiz.solstice.core.game.scenes.game

import cc.lapiz.solstice.core.data.*
import cc.lapiz.solstice.core.game.*
import cc.lapiz.solstice.core.ui.Colors
import cc.lapiz.solstice.core.ui.elements.*
import cc.lapiz.solstice.core.ui.layout.*
import cc.lapiz.solstice.core.ui.px
import cc.lapiz.solstice.core.ui.style.*

class GameScene : Scene() {
	override fun initUI() = UIStack(Attrib, 0f, Direction.Column, Flex.SpaceBetween, Align.Center).apply {
		stack(Attrib.fullSize(heightRatio = 0.2f.px).fill(Colors.Primary)) {

		}
		stack(Attrib.fullSize(heightRatio = 0.2f.px).fill(Colors.Primary)) {

		}
	}

	override fun onEnter() {
		super.onEnter()
		val testEntity = ecs.createEntity(Transform())

	}
}