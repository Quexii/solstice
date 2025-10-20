package cc.lapiz.solstice.core

import cc.lapiz.solstice.core.assets.Assets
import cc.lapiz.solstice.core.editor.Editor
import cc.lapiz.solstice.core.event.Event
import cc.lapiz.solstice.core.event.Events
import cc.lapiz.solstice.core.game.SceneManager
import cc.lapiz.solstice.core.game.components.ComponentRegistry
import cc.lapiz.solstice.core.rendering.RenderSystem
import cc.lapiz.solstice.core.resource.IO
import cc.lapiz.solstice.core.time.Timer
import cc.lapiz.solstice.core.ui.imgui.ImGuiCtx
import cc.lapiz.solstice.core.utils.Props
import cc.lapiz.solstice.core.utils.toByteArray
import cc.lapiz.solstice.core.window.Display
import imgui.ImFontConfig
import imgui.flag.ImGuiConfigFlags
import org.lwjgl.glfw.GLFW

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
		ComponentRegistry.initInternal()
		Assets.init()
		Assets.loadTick()
		ImGuiCtx.init {
			it.addConfigFlags(ImGuiConfigFlags.DockingEnable)
			it.fonts.clear()

			val fontConfig = ImFontConfig()
			val font = it.fonts.addFontFromMemoryTTF(
				IO.getBuffer("fonts/share_tech_mono/Regular.ttf").toByteArray(),
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
			Assets.loadTick()
			Timer.update()
			update(Timer.deltaTime)
			GLFW.glfwPollEvents()
			Events.Queue.poll()?.let { event(it) }
			render()
			GLFW.glfwSwapBuffers(Display.handle)
		}

		ImGuiCtx.cleanup()
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
			ImGuiCtx.newFrame()
			Editor.imgui()
			ImGuiCtx.render()
		}
	}
}