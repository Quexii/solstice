package cc.lapiz.solstice.core.dev

import cc.lapiz.solstice.core.assets.Asset
import cc.lapiz.solstice.core.assets.Assets
import cc.lapiz.solstice.core.assets.meta.MetaGen
import cc.lapiz.solstice.core.assets.meta.MetaPrefab
import cc.lapiz.solstice.core.assets.meta.MetaTexture
import cc.lapiz.solstice.core.assets.types.Sprite
import cc.lapiz.solstice.core.game.components.ImSerialize
import cc.lapiz.solstice.core.ui.imgui.GridItem
import cc.lapiz.solstice.core.ui.imgui.dynamicItemGrid
import cc.lapiz.solstice.core.game.GameObject
import cc.lapiz.solstice.core.game.Scene
import cc.lapiz.solstice.core.game.SceneManager
import cc.lapiz.solstice.core.game.components.ComponentRegistry
import cc.lapiz.solstice.core.game.components.meta.ComponentName
import cc.lapiz.solstice.core.rendering.RenderSystem
import cc.lapiz.solstice.core.time.Timer
import cc.lapiz.solstice.core.ui.imgui.ImGuiCtx
import cc.lapiz.solstice.core.ui.imgui.table2Column
import cc.lapiz.solstice.core.utils.argbToAbgr
import cc.lapiz.solstice.core.utils.getAnnotations
import cc.lapiz.solstice.core.window.Display
import imgui.*
import imgui.flag.ImGuiTableColumnFlags
import imgui.flag.ImGuiTableFlags
import imgui.type.ImFloat
import imgui.type.ImInt
import org.joml.Vector2i
import org.lwjgl.opengl.GL33C
import java.nio.file.Path
import kotlin.reflect.full.createInstance
import kotlinx.serialization.json.buildJsonObject

private typealias MetaTextureType = MetaTexture.Serialized.TextureType
private typealias MetaTextureFilter = MetaTexture.Serialized.TextureFilter
private typealias MetaTextureWrap = MetaTexture.Serialized.TextureWrap

object Editor {
    private lateinit var scene: Scene
    private var selectedInspectorItem: Any? = null
    private var extraSelecedData: Any? = null
    fun imgui() {
        scene = SceneManager.getCurrent()

        hierarchy()
        viewport()
        inspector()
        assets()
    }

    private fun hierarchy() {
        ImGuiCtx.window("Hierarchy") {

            ImGuiCtx.withPopup("hierarchy_create_menu")
            if (ImGui.beginPopup("hierarchy_create_menu")) {
                ImGui.button("Create Empty")
                ImGui.endPopup()
            }

            val size = ImGui.getContentRegionAvail()
            scene.gameObjects.iterator().forEach {
                if (ImGui.button(it.name, ImVec2(size.x, 20f))) {
                    selectedInspectorItem = it
                }
            }
        }
    }

    private fun inspector() {
        ImGuiCtx.window("Inspector") {
            if (selectedInspectorItem != null) {
                when (selectedInspectorItem) {
                    is GameObject -> {
                        (selectedInspectorItem!! as GameObject).components.values.forEach {
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
                            println((selectedInspectorItem!! as GameObject).getJson())
                        }


                        ImGui.setNextWindowPos(buttonMin.x, buttonMax.y)
                        ImGui.setNextWindowSize(size.x, 0f)

                        if (ImGui.beginPopup("add_component_popup")) {
                            ComponentRegistry.getAll().forEach {
                                if (ImGui.menuItem((getAnnotations(it).find { it is ComponentName } as ComponentName?)?.name ?: this::class.simpleName ?: "")) {
                                    val instance = it.createInstance()
                                    (selectedInspectorItem!! as GameObject).addComponent(instance)
                                }
                            }
                            ImGui.endPopup()
                        }
                    }

                    is Asset -> when ((selectedInspectorItem!! as Asset).meta) {
                        is MetaTexture.Serialized -> {
                            val selectedData = (extraSelecedData as Array<ImInt>)
                            val meta = (selectedInspectorItem!! as Sprite).meta as MetaTexture.Serialized

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
                                (selectedInspectorItem!! as Sprite).update(selectedData.map { it.data[0] }
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

    private fun viewport() {
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
            ImGui.getWindowDrawList().addText(pos.x + max.x - fpsStrSz.x - 8f, pos.y + min.y + 4f, (-1).argbToAbgr(), fpsStr)
            ImGui.getWindowDrawList().addText(pos.x + max.x - frameCountStrSz.x - 8f, pos.y + min.y + 4f + 16f, (-1).argbToAbgr(), frameCountStr)
            ImGui.getWindowDrawList().addText(pos.x + max.x - deltaStrSz.x - 8f, pos.y + min.y + 4f + 16f * 2, (-1).argbToAbgr(), deltaStr)
            ImGui.getWindowDrawList().addText(pos.x + max.x - totalStrSz.x - 8f, pos.y + min.y + 4f + 16f * 3, (-1).argbToAbgr(), totalStr)

//            ImGui.getWindowDrawList().addRect(pos + min, pos + max, (-1).argbToAbgr())
        }
    }

    private fun assets() {
        ImGuiCtx.window("Assets") {
            if (ImGui.button("Create Prefab")) {
                val timestamp = System.currentTimeMillis()
                Assets.registerVirtualAsset("prefabs/prefab_$timestamp.prefab") { path ->
                    MetaPrefab(
                        path,
                        MetaPrefab.Serialized(
                            name = "Prefab $timestamp",
                            definition = buildJsonObject { },
                            tags = listOf("runtime"),
                            template = false,
                        )
                    )
                }
            }
            ImGui.separator()
            val list = Assets.getAll()
            dynamicItemGrid(
                items = list.map {
                    GridItem(it::class.java.simpleName, icon = { min, max ->
                        when (it) {
                            is Sprite -> addImage(it.textureId().toLong(), min.plus(8f, 8f), max.minus(8f, 8f))
                        }
                    })
                },
                maxItemSize = 100f,
                onItemDoubleClick = { index, item ->
                    selectedInspectorItem = list[index]
                    when (list[index]) {
                        is Sprite -> extraSelecedData = arrayOf(
                            ImInt(MetaTextureType.entries.indexOf(((selectedInspectorItem!! as Sprite).meta as MetaTexture.Serialized).type)),
                            ImInt(MetaTextureFilter.entries.indexOf(((selectedInspectorItem!! as Sprite).meta as MetaTexture.Serialized).filter)),
                            ImInt(MetaTextureWrap.entries.indexOf(((selectedInspectorItem!! as Sprite).meta as MetaTexture.Serialized).wrap))
                        )
                    }
                }
            )
        }
    }
}