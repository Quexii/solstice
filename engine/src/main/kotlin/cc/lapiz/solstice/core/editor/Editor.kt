package cc.lapiz.solstice.core.editor

import cc.lapiz.solstice.core.editor.view.*
import cc.lapiz.solstice.core.game.Scene
import cc.lapiz.solstice.core.game.SceneManager
import imgui.ImGui

object Editor {
    var currentScene: Scene? = null
    private val views = listOf(
        EditorViewHierarchy(),
        EditorViewInspector(),
        EditorViewViewport(),
        EditorViewAssets()
    )

    private val toggledViews = buildMap<EditorView, Boolean> {
        views.forEach { put(it, false) }
    }.toMutableMap()
    var inspectorItem: InspectorItem<*>? = null
        private set

    init {
        toggledViews[views.first { it is EditorViewHierarchy }] = true
        toggledViews[views.first { it is EditorViewInspector }] = true
        toggledViews[views.first { it is EditorViewViewport }] = true
        toggledViews[views.first { it is EditorViewAssets }] = true
    }

    fun imgui() {
        currentScene = SceneManager.getCurrent()

        if (ImGui.beginMainMenuBar()) {
            if (ImGui.beginMenu("File")) {
                if (ImGui.menuItem("New Scene")) {
                }
                if (ImGui.menuItem("Open...")) {
                }
                if (ImGui.menuItem("Save", "Ctrl+S")) {
                }
                ImGui.separator()
                if (ImGui.menuItem("Exit")) {
                }
                ImGui.endMenu()
            }

            if (ImGui.beginMenu("View")) {
                views.forEach { view ->
                    if (ImGui.menuItem(view.name, toggledViews[view]!!)) {
                        toggledViews[view] = !toggledViews[view]!!
                    }
                }
                ImGui.endMenu()
            }

            if (ImGui.beginMenu("Help")) {
                if (ImGui.menuItem("About")) {
                }
                ImGui.endMenu()
            }

            ImGui.endMainMenuBar()
        }

        toggledViews.forEach { view, state ->
            if (state) view.draw()
        }
    }

    fun <T> selectInspectorItem(item: InspectorItem<T>?) {
        inspectorItem = item
    }
}