package cc.lapiz.solstice.core.editor.view

import cc.lapiz.solstice.core.rendering.RenderSystem
import cc.lapiz.solstice.core.time.Timer
import cc.lapiz.solstice.core.ui.imgui.ImGuiCtx
import cc.lapiz.solstice.core.utils.argbToAbgr
import cc.lapiz.solstice.core.window.Display
import imgui.ImGui
import imgui.ImVec2
import imgui.flag.ImGuiHoveredFlags
import imgui.type.ImBoolean
import org.joml.Vector2i
import org.lwjgl.opengl.GL33C

class EditorViewViewport : EditorView("View") {
    override fun draw() {
        ImGuiCtx.window("Scene View") {

            val pos = ImGui.getWindowPos()
            val min = ImGui.getWindowContentRegionMin()
            val max = ImGui.getWindowContentRegionMax()
            val size = ImGui.getContentRegionAvail()
            if (size.x != Display.contentSize().x.toFloat() || size.y != Display.contentSize().y.toFloat()) {
                Display.contentSize(Vector2i(size.x.toInt(), size.y.toInt()))
                GL33C.glViewport(0, 0, Display.contentSize().x, Display.contentSize().y)
            }
            ImGui.image(RenderSystem.framebuffer().textureId().toLong(), size.x, size.y)
            val fpsStr = "${Timer.fps}: FPS"
            val frameCountStr = "${Timer.frameCount}: Frame Count"
            val deltaStr = "${Timer.deltaTime}: Delta Time"
            val totalStr = "${Timer.totalTime}: Total TIme"

            val fpsStrSz = ImGuiCtx.io().fontDefault.calcTextSizeA(16f, 1000f, 1000f, fpsStr)
            val frameCountStrSz = ImGuiCtx.io().fontDefault.calcTextSizeA(16f, 1000f, 1000f, frameCountStr)
            val deltaStrSz = ImGuiCtx.io().fontDefault.calcTextSizeA(16f, 1000f, 1000f, deltaStr)
            val totalStrSz = ImGuiCtx.io().fontDefault.calcTextSizeA(16f, 1000f, 1000f, totalStr)
            ImGui.getWindowDrawList()
                .addText(pos.x + max.x - fpsStrSz.x - 8f, pos.y + min.y + 4f, (-1).argbToAbgr(), fpsStr)
            ImGui.getWindowDrawList().addText(
                pos.x + max.x - frameCountStrSz.x - 8f,
                pos.y + min.y + 4f + 16f,
                (-1).argbToAbgr(),
                frameCountStr
            )
            ImGui.getWindowDrawList()
                .addText(pos.x + max.x - deltaStrSz.x - 8f, pos.y + min.y + 4f + 16f * 2, (-1).argbToAbgr(), deltaStr)
            ImGui.getWindowDrawList()
                .addText(pos.x + max.x - totalStrSz.x - 8f, pos.y + min.y + 4f + 16f * 3, (-1).argbToAbgr(), totalStr)
        }
    }
}