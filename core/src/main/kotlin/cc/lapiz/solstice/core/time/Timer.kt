package cc.lapiz.solstice.core.time

abstract class Timer {
	var deltaTime: Float = 0f
		protected set
	var fps: Int = 0
		protected set

	abstract fun update()
}