package cc.lapiz.solstice.core.ui.imgui

import cc.lapiz.solstice.core.serialization.ImGuiThemeJSON
import cc.lapiz.solstice.core.serialization.applyToImGui
import cc.lapiz.solstice.core.resource.IO
import cc.lapiz.solstice.core.window.Display
import imgui.ImGui
import imgui.ImGuiIO
import imgui.extension.imnodes.ImNodes
import imgui.flag.ImGuiConfigFlags
import imgui.flag.ImGuiMouseButton
import imgui.flag.ImGuiWindowFlags
import imgui.gl3.*
import imgui.glfw.*
import imgui.internal.*
import imgui.type.ImBoolean
import kotlinx.serialization.json.Json
import org.lwjgl.glfw.GLFW

object ImGuiCtx {
    private val imGuiGlfw = ImGuiImplGlfw()
    private val imGuiGl3 = ImGuiImplGl3()

    fun window(title: String, flags: Int = 0, content: () -> Unit) {
        ImGui.begin(title, flags)
        content()
        ImGui.end()
    }

    fun withPopup(id: String) {
        if (ImGui.isWindowHovered() &&
            !ImGui.isAnyItemHovered() &&
            !ImGui.isAnyItemActive() &&
            ImGui.isMouseClicked(ImGuiMouseButton.Right)
        ) {
            ImGui.openPopup(id)
        }
    }

    fun withTopPopup(id: String) {
        if (ImGui.isWindowHovered() &&
            ImGui.isMouseClicked(ImGuiMouseButton.Right)
        ) {
            ImGui.openPopup(id)
        }
    }

    fun init(config: (ImGuiIO) -> Unit): ImGuiContext {
        val ctx = ImGui.createContext()
        ImNodes.createContext()
        ImNodes.editorContextCreate()
        config(io())
        imGuiGlfw.init(Display.handle, true)
        imGuiGl3.init("#version 330 core")
        applyStyle()
        return ctx
    }

    private fun applyStyle() = style().apply {
        this.windowRounding = 6f;
        this.childRounding = 4f;
        this.frameRounding = 4f;
        this.popupRounding = 6f;
        this.scrollbarRounding = 2f;
        this.grabRounding = 2f;
        this.tabRounding = 4f;

        this.windowPadding.set(10f, 10f);
        this.framePadding.set(6f, 5f);
        this.cellPadding.set(4f, 2f);
        this.itemSpacing.set(8f, 6f);
        this.itemInnerSpacing.set(4f, 3f);

        this.windowBorderSize = 1f;
        this.childBorderSize = 0f;
        this.popupBorderSize = 1f;
        this.frameBorderSize = 0f;
        this.tabBorderSize = 0f;


        val string = IO.getText("one_dark_imgui.json")
        Json.decodeFromString<ImGuiThemeJSON>(string).applyToImGui()
    }

    fun newFrame() {
        imGuiGl3.newFrame()
        imGuiGlfw.newFrame()
        ImGui.newFrame()
        ImGui.dockSpaceOverViewport(ImGui.getMainViewport().id)
    }

    fun render() {
        ImGui.render()
        imGuiGl3.renderDrawData(ImGui.getDrawData())

        if (io().configFlags and ImGuiConfigFlags.ViewportsEnable != 0) {
            val backupCurrentContext = GLFW.glfwGetCurrentContext()
            ImGui.updatePlatformWindows()
            ImGui.renderPlatformWindowsDefault()
            GLFW.glfwMakeContextCurrent(backupCurrentContext)
        }
    }

    fun cleanup() {
        imGuiGl3.shutdown()
        ImGui.destroyContext()
    }

    fun io() = ImGui.getIO()
    fun style() = ImGui.getStyle()
}