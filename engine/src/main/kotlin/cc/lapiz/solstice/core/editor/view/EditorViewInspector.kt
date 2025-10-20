package cc.lapiz.solstice.core.editor.view

import cc.lapiz.solstice.core.assets.Asset
import cc.lapiz.solstice.core.assets.Assets
import cc.lapiz.solstice.core.assets.meta.*
import cc.lapiz.solstice.core.assets.types.Sprite
import cc.lapiz.solstice.core.editor.Editor
import cc.lapiz.solstice.core.game.GameObject
import cc.lapiz.solstice.core.game.components.ComponentRegistry
import cc.lapiz.solstice.core.game.components.ImSerialize
import cc.lapiz.solstice.core.game.components.meta.ComponentName
import cc.lapiz.solstice.core.ui.imgui.ImGuiCtx
import cc.lapiz.solstice.core.utils.getAnnotations
import imgui.ImGui
import imgui.type.ImInt
import java.nio.file.Path
import kotlin.reflect.full.createInstance

class EditorViewInspector: EditorView("Inspector") {
    override fun draw() {
        ImGuiCtx.window("Inspector") {
            if (Editor.inspectorItem != null) {
                when (Editor.inspectorItem!!.data) {
                    is GameObject -> {
                        (Editor.inspectorItem!!.data!! as GameObject).components.values.forEach {
                            if (it is ImSerialize) {
                                if (ImGui.collapsingHeader(it::class.simpleName.toString())) {
                                    it.drawImGui()
                                }
                            } else {
                                ImGui.text(it::class.simpleName.toString())
                            }
                        }
                        val size = ImGui.getContentRegionAvail()
                        val width = (size.x - ImGui.getStyle().itemSpacing.x) / 2f

                        if (ImGui.button("Add Component", width, 24f)) {
                            ImGui.openPopup("add_component_popup")
                        }

                        val buttonMin = ImGui.getItemRectMin()
                        val buttonMax = ImGui.getItemRectMax()

                        ImGui.sameLine()

                        if (ImGui.button("Copy JSON", width, 24f)) {
                            println((Editor.inspectorItem!!.data!! as GameObject).getJson())
                        }


                        ImGui.setNextWindowPos(buttonMin.x, buttonMax.y)
                        ImGui.setNextWindowSize(size.x, 0f)

                        if (ImGui.beginPopup("add_component_popup")) {
                            ComponentRegistry.getAll().forEach {
                                if (ImGui.menuItem((getAnnotations(it).find { it is ComponentName } as ComponentName?)?.name
                                        ?: this::class.simpleName ?: "")) {
                                    val instance = it.createInstance()
                                    (Editor.inspectorItem!!.data!! as GameObject).addComponent(instance)
                                }
                            }
                            ImGui.endPopup()
                        }
                    }

                    is Asset -> when ((Editor.inspectorItem!!.data!! as Asset).meta) {
                        is MetaTexture.Serialized -> {
                            val selectedData = (Editor.inspectorItem!!.data as Array<ImInt>)
                            val meta = (Editor.inspectorItem!!.data!! as Sprite).meta as MetaTexture.Serialized

                            ImGui.columns(2)
                            ImGui.text("Type")
                            ImGui.text("Filter")
                            ImGui.text("Wrap")
                            ImGui.nextColumn()
                            ImGui.combo("Type", selectedData[0], MetaTextureType.entries.map { it.name }.toTypedArray())
                            ImGui.combo(
                                "Filter",
                                selectedData[1],
                                MetaTextureFilter.entries.map { it.name }.toTypedArray()
                            )
                            ImGui.combo("Wrap", selectedData[2], MetaTextureWrap.entries.map { it.name }.toTypedArray())
                            ImGui.columns(1)
                            if (ImGui.button("Apply")) {
                                (Editor.inspectorItem!!.data!! as Sprite).update(selectedData.map { it.data[0] }
                                    .toTypedArray())
                                Assets.updateAssetMetadata(
                                    MetaGen.fromCachedPath(MetaGen.jsonFile(Path.of(meta.path)).toPath()), meta.copy(
                                        type = MetaTextureType.entries[selectedData[0].data[0]],
                                        filter = MetaTextureFilter.entries[selectedData[1].data[0]],
                                        wrap = MetaTextureWrap.entries[selectedData[2].data[0]],
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}