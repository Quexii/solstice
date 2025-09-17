package cc.lapiz.solstice.desktop.platform

import cc.lapiz.solstice.core.time.Timer
import org.lwjgl.glfw.GLFW.glfwGetTime

class GLFWTimer : Timer() {
	private var lastTime: Double = glfwGetTime()
	var totalTime: Double = 0.0
		private set

	override fun update() {
		val now = glfwGetTime()
		var dt = now - lastTime

		if (dt > 0.25) dt = 0.25

		deltaTime = dt.toFloat()
		totalTime += dt
		lastTime = now
	}
}
