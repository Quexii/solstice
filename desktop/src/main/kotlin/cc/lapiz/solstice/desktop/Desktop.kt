package cc.lapiz.solstice.desktop

import cc.lapiz.solstice.core.*
import cc.lapiz.solstice.core.event.WindowEvent
import cc.lapiz.solstice.core.input.*
import cc.lapiz.solstice.core.rendering.platform.*
import cc.lapiz.solstice.desktop.platform.*
import cc.lapiz.solstice.desktop.rendering.paltform.*
import cc.lapiz.solstice.desktop.window.*
import org.lwjgl.glfw.GLFW.*

class Desktop : Platform() {
	private val window = Window()

	override fun start() {
		window.create("Solstice", 800, 600, false)
		GameCore.EventQueue.push(WindowEvent.Resize(800, 600))

		timer = GLFWTimer()
		Input.setHandler(GLFWInputHandler(window))
		Gr.init(GlFunctions(), GlTypes(), GlCapabilites())

		GameCore.start()
		super.start()

		window.run { handle ->
			timer.update()
			update(timer.deltaTime)
			Graphics.clearColor(0.1f, 0.1f, 0.1f, 0f)
			Graphics.clear(Graphics.COLOR_BUFFER_BIT)
			glfwPollEvents()
			GameCore.EventQueue.poll()?.let { event(it) }
			render()
			glfwSwapBuffers(handle)
		}

		GameCore.stop()

		window.destroy()
	}
}