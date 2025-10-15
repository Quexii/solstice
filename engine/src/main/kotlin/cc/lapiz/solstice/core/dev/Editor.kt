package cc.lapiz.solstice.dev

import cc.lapiz.solstice.core.game.components.ImSerialize
import cc.lapiz.solstice.data.Vector2
import cc.lapiz.solstice.game.Scene
import cc.lapiz.solstice.game.SceneManager
import cc.lapiz.solstice.rendering.RenderSystem
import cc.lapiz.solstice.core.ui.Colors
import cc.lapiz.solstice.core.ui.EditorUI
import cc.lapiz.solstice.game.GameObject
import cc.lapiz.solstice.ui.imgui.ImGui
import cc.lapiz.solstice.utils.argbToAbgr
import cc.lapiz.solstice.window.Display
import imgui.ImVec2
import imgui.flag.ImGuiCol
import org.joml.Vector2i
import org.lwjgl.opengl.GL33C

object Editor {

    private lateinit var scene: Scene
    private var selectedObject: GameObject? = null
    fun imgui() {
        scene = SceneManager.getCurrent()

        hierarchy()
        viewport()
        inspector()
    }

    private fun hierarchy() {
        ImGui.begin("Hierarchy")
        val size = imgui.ImGui.getContentRegionAvail()
        scene.gameObjects.iterator().forEach {
            if (ImGui.button(it.name, ImVec2(size.x, 20f))) {
                this.selectedObject = it
            }
        }
        ImGui.end()
    }

    private fun inspector() {
        ImGui.begin("Inspector")
        if (selectedObject != null) {
            selectedObject!!.components.values.forEach {
                if (it is ImSerialize) {
                    if (ImGui.collapsingHeader(it::class.simpleName.toString())) {
                        it.drawImGui()
                    }
                } else {
                    ImGui.text(it::class.simpleName.toString())
                }
            }
        }
        ImGui.end()
    }

    private fun viewport() {
        ImGui.begin("Scene View")
        val size = imgui.ImGui.getContentRegionAvail()
        if (size.x != Display.contentSize().x.toFloat() || size.y != Display.contentSize().y.toFloat()) {
            Display.contentSize(Vector2i(size.x.toInt(), size.y.toInt()))
            GL33C.glViewport(0, 0, Display.contentSize().x, Display.contentSize().y)
        }
        ImGui.image(RenderSystem.framebuffer().textureId(), size.x, size.y)
        ImGui.end()
    }
}