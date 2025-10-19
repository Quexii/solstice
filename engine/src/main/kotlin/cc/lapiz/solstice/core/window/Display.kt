package cc.lapiz.solstice.core.window

import cc.lapiz.solstice.core.event.Events
import cc.lapiz.solstice.core.event.WindowEvent
import cc.lapiz.solstice.core.options.Options
import cc.lapiz.solstice.core.utils.Props
import cc.lapiz.solstice.core.utils.logger
import org.joml.Vector2i
import org.lwjgl.glfw.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.*

object Display {
	private var size = Vector2i()
	private var contentSize = Vector2i()
	private var position = Vector2i()
	private var focused = false

	private val LOGGER = logger(this::class.java)

	var handle = 0L
		private set

	var shouldClose
		get() = glfwWindowShouldClose(handle)
		set(value) = glfwSetWindowShouldClose(handle, value)

	fun create(options: WindowOptions) {
		size.set(options.width, options.height)
		contentSize = size
		glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err))

		if (!glfwInit()) {
			LOGGER.error("Unable to initialize GLFW")
			throw IllegalStateException("Unable to initialize GLFW")
		}

		glfwDefaultWindowHints()
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
		glfwWindowHint(GLFW_RESIZABLE, if (options.resizable) GLFW_TRUE else GLFW_FALSE)
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
		handle = glfwCreateWindow(options.width, options.height, options.title, 0, 0)
		if (handle == 0L) {
			LOGGER.error("Failed to create the GLFW window")
			throw RuntimeException("Failed to create the GLFW window")
		}

		glfwSetWindowSizeCallback(handle) { _, width, height ->
			size.set(width, height)
			if (!Props.EDITOR) contentSize.set(width, height)
			Events.Queue.push(WindowEvent.Resize(width, height))
			GL33C.glViewport(0, 0, contentSize.x, contentSize.y)
		}

		glfwSetWindowPosCallback(handle) { _, x, y ->
			position.set(x, y)
			Events.Queue.push(WindowEvent.Move(x, y))
		}

		glfwSetWindowFocusCallback(handle) { _, focused ->
			Display.focused = focused
			Events.Queue.push(WindowEvent.Focus(focused))
		}

		glfwMakeContextCurrent(handle)
		glfwSwapInterval(if (Options.get(Options.VSYNC)) 1 else 0)

		GL.createCapabilities()
	}

	fun cleanup() {
		glfwSetErrorCallback(null)?.free()
		glfwDestroyWindow(handle)
		glfwTerminate()
	}

	fun width() = size.x
	fun height() = size.y
	fun position() = position
	fun focused() = focused

	fun contentSize(size: Vector2i) = contentSize.set(size)
	fun contentSize() = contentSize
}