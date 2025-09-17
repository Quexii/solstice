package cc.lapiz.solstice.core.time

abstract class Timer {
	var deltaTime: Float = 0f
		protected set

	abstract fun update()
}