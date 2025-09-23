package cc.lapiz.solstice.core.game.ecs.components

import cc.lapiz.solstice.core.data.Target

class GoToTarget(val targets: MutableList<Target> = mutableListOf<Target>()) {
	fun addTarget(target: Target) {
		targets.add(target)
	}

	override fun toString(): String {
		return "GoToTarget(targets=${targets.joinToString()})"
	}
}