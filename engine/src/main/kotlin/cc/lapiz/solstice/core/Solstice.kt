package cc.lapiz.solstice.core

import cc.lapiz.solstice.core.assets.Assets
import cc.lapiz.solstice.event.*
import cc.lapiz.solstice.game.*
import cc.lapiz.solstice.dev.Editor
import cc.lapiz.solstice.font.FontManager
import cc.lapiz.solstice.rendering.*
import cc.lapiz.solstice.time.*
import cc.lapiz.solstice.ui.imgui.ImGui
import cc.lapiz.solstice.utils.Props
import cc.lapiz.solstice.utils.toByteArray
import cc.lapiz.solstice.window.*
import imgui.ImFontConfig
import imgui.flag.ImGuiConfigFlags
import org.lwjgl.glfw.*

class Solstice(val entrypoint: Entrypoint) {
	companion object {
		fun entry(entrypoint: Entrypoint, args: Array<String>) {
			Props.init(args)
			Solstice(entrypoint).run()
		}
	}

	fun run() {
		Display.create(entrypoint.windowOptions)
		RenderSystem.init()
		Assets.init()
		ImGui.init {
			it.addConfigFlags(ImGuiConfigFlags.DockingEnable)
			it.fonts.clear()

			val fontConfig = ImFontConfig()
			val font = it.fonts.addFontFromMemoryTTF(
				FontManager.Default.data("regular")!!.toByteArray(),
				16f,
				fontConfig,
				it.fonts.glyphRangesDefault
			)
			it.fonts.build()
			if (font != null) {
				it.fontDefault = font
			}
		}
		entrypoint.start()
		SceneManager.setScene(entrypoint.mainScene)

		GLFW.glfwShowWindow(Display.handle)
		while (!Display.shouldClose) {
			Timer.update()
			update(Timer.deltaTime)
			GLFW.glfwPollEvents()
			Events.Queue.poll()?.let { event(it) }
			render()
			GLFW.glfwSwapBuffers(Display.handle)
		}

		ImGui.cleanup()
		Assets.cleanup()
		Display.cleanup()
	}

	private fun update(delta: Float) {
		RenderSystem.Camera.projectWorld()
		SceneManager.update(delta)
	}

	private fun event(event: Event) {
		SceneManager.handleEvents(event)
	}

	private fun render() {
		RenderSystem.framebuffer().bind()
		RenderSystem.clear(0.1f, 0.1f, 0.1f, 1f)
		RenderSystem.enableAlpha()
		RenderSystem.enableDepth()
		RenderSystem.Camera.projectWorld()
		SceneManager.render()
		RenderSystem.framebuffer().unbind()

		if (Props.EDITOR) {
			ImGui.newFrame()
			Editor.imgui()
			ImGui.render()
		}
	}
}