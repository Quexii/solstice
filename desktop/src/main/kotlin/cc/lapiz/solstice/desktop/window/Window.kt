package cc.lapiz.solstice.desktop.window

import cc.lapiz.solstice.core.GameCore
import cc.lapiz.solstice.core.event.WindowEvent
import cc.lapiz.solstice.core.options.*
import cc.lapiz.solstice.core.utils.*
import org.lwjgl.glfw.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.*

class Window {
	var handle = 0L
		private set

	private val LOGGER = logger(this::class.java)

	var shouldClose
		get() = glfwWindowShouldClose(handle)
		set(value) = glfwSetWindowShouldClose(handle, value)

	fun create(title: String, width: Int, height: Int, resizable: Boolean) {
		glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err))

		if (!glfwInit()) {
			LOGGER.error("Unable to initialize GLFW")
			throw IllegalStateException("Unable to initialize GLFW")
		}

		glfwDefaultWindowHints()
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
		glfwWindowHint(GLFW_RESIZABLE, if (resizable) GLFW_TRUE else GLFW_FALSE)
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
		handle = glfwCreateWindow(width, height, title, 0, 0)
		if (handle == 0L) {
			LOGGER.error("Failed to create the GLFW window")
			throw RuntimeException("Failed to create the GLFW window")
		}

		glfwSetWindowSizeCallback(handle) { _, width, height ->
			GameCore.EventQueue.push(WindowEvent.Resize(width, height))
		}

		glfwSetWindowPosCallback(handle) { _, x, y ->
			GameCore.EventQueue.push(WindowEvent.Move(x, y))
		}

		glfwSetWindowFocusCallback(handle) { _, focused ->
			GameCore.EventQueue.push(WindowEvent.Focus(focused))
		}

		glfwMakeContextCurrent(handle)
		glfwSwapInterval(if (Options.get(Options.VSYNC)) 1 else 0)

		GL.createCapabilities()
	}

	fun run(block: (Long) -> Unit) {
		glfwShowWindow(handle)
		while (!shouldClose) {
			block(handle)
		}
	}

	fun destroy() {
		glfwDestroyWindow(handle)
		glfwTerminate()
	}
}