package cc.lapiz.solstice.core.editor.view

import cc.lapiz.solstice.core.editor.Editor
import cc.lapiz.solstice.core.editor.InspectorItem
import cc.lapiz.solstice.core.ui.imgui.ImGuiCtx
import imgui.ImGui
import imgui.ImVec2

class EditorViewHierarchy: EditorView("Hierarchy") {
    override fun draw() {
        if (Editor.currentScene != null) {
            ImGuiCtx.window("Hierarchy") {



                ImGuiCtx.withPopup("hierarchy_create_menu")
                if (ImGui.beginPopup("hierarchy_create_menu")) {
                    ImGui.button("Create Empty")
                    ImGui.endPopup()
                }

                val size = ImGui.getContentRegionAvail()
                Editor.currentScene!!.gameObjects.iterator().forEach {
                    if (ImGui.button(it.name, ImVec2(size.x, 20f))) {
                        Editor.selectInspectorItem(InspectorItem(it))
                    }
                }
            }
        }
    }
}