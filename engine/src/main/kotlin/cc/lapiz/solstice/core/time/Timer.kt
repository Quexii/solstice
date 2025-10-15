package cc.lapiz.solstice.time

import org.lwjgl.glfw.GLFW.glfwGetTime

object Timer {
	var deltaTime: Float = 0f
		private set
	var fps: Int = 0
		private set

	private var lastTime: Double = glfwGetTime()
	var totalTime: Double = 0.0
		private set
	var frameCount: Int = 0
		private set

	fun update() {
		val now = glfwGetTime()
		var dt = now - lastTime
		totalTime += dt
		frameCount++

		if (totalTime >= 1.0) {
			fps = frameCount
			frameCount = 0
			totalTime -= 1.0
		}

		if (dt > 0.25) dt = 0.25

		deltaTime = dt.toFloat()
		lastTime = now
	}
}