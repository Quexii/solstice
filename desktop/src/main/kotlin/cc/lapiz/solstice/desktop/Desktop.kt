package cc.lapiz.solstice.desktop

import cc.lapiz.solstice.core.*
import cc.lapiz.solstice.core.event.Events
import cc.lapiz.solstice.core.input.*
import cc.lapiz.solstice.core.rendering.nanovg.*
import cc.lapiz.solstice.core.rendering.nanovg.platform.*
import cc.lapiz.solstice.core.rendering.platform.*
import cc.lapiz.solstice.core.window.*
import cc.lapiz.solstice.desktop.platform.*
import cc.lapiz.solstice.desktop.rendering.paltform.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.nanovg.*

private typealias DesktopWindow = cc.lapiz.solstice.desktop.window.Window
class Desktop : Platform() {
	private val window = DesktopWindow()

	override fun start() {
		window.create("Solstice", 800, 600, false)

		timer = GLFWTimer()
		Input.setHandler(GLFWInputHandler(window))
		Gr.init(GlFunctions(), GlTypes(), GlCapabilites())
		NVcanvas.init(NVgl3(), NanoVGGL3.NVG_ANTIALIAS or NanoVGGL3.NVG_STENCIL_STROKES)

		Window.initialize(800, 600)
		super.start()

		window.run { handle ->
			timer.update()
			update(timer.deltaTime)
			glfwPollEvents()
			Events.Queue.poll()?.let { event(it) }
			render()
			glfwSwapBuffers(handle)
		}

		window.destroy()
	}
}