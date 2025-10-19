package cc.lapiz.solstice.core.game.components.impl

import cc.lapiz.solstice.core.assets.Assets
import cc.lapiz.solstice.core.assets.types.Sprite
import cc.lapiz.solstice.core.game.components.meta.ComponentName
import cc.lapiz.solstice.core.ui.Colors
import cc.lapiz.solstice.core.rendering.pipeline.shader.UniformScope
import cc.lapiz.solstice.core.rendering.texture.TextureUtil
import cc.lapiz.solstice.core.ui.imgui.*
import imgui.ImGui
import imgui.ImVec2
import imgui.flag.ImGuiKey
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImString
import kotlinx.serialization.json.*
import java.nio.file.Path

@ComponentName("Sprite Renderer")
class SpriteRenderer : MeshRenderer() {
    var sprite: Sprite? = null
    var color = Colors.White
    private var selectingSprite = false
    private var searchFilter = ImString("", 256)

    override fun preShader(): Boolean {
        if (sprite == null) return false
        TextureUtil.bindTexture(sprite!!.textureId())

        return true
    }

    override fun applyUniforms(scope: UniformScope) {
        if (sprite != null) {
            scope.sampler2D("Texture", 0)
            scope.vec4("Color", color.r, color.g, color.b, color.a)
        }
    }

    override fun drawImGui() {
        table2Column("##Transform") {
            tableRow("Color", color)
            tableRow("Sprite", sprite) {
                if (ImGui.button(
                    if (sprite == null) "none" else {
                        val path = Path.of(sprite!!.meta.path)
                        path.fileName.toString()
                    }
                )
            ) {
                selectingSprite = true
                searchFilter.set("", false)
            }
            }
        }

        if (selectingSprite) {
            ImGuiCtx.window("Select Sprite", ImGuiWindowFlags.NoCollapse or ImGuiWindowFlags.Modal or ImGuiWindowFlags.NoDocking) {
                if (ImGui.isWindowFocused() && ImGui.isKeyPressed(ImGuiKey.Escape)) {
                    selectingSprite = false
                }

                val assets = Assets.getType<Sprite>()
                ImGui.columns(2)
                if (ImGui.button("Close")) {
                    selectingSprite = false
                }
                ImGui.nextColumn()
                ImGui.inputText("Search", searchFilter)
                ImGui.columns(1)
                dynamicItemGrid(
                    assets.filter {
                        val path = Path.of(it.meta.path).fileName.toString()
                        path.startsWith(searchFilter.get()) or path.contains(searchFilter.get())
                    }.map { GridItem(
                        label = Path.of(it.meta.path).fileName.toString(),
                        icon = { min: ImVec2, max: ImVec2 -> addImage(it.textureId().toLong(), min.plus(8f, 8f), max.minus(8f, 8f)) },
                        data = it
                    ) },
                    onItemDoubleClick = { _, item ->
                        sprite = item.data as Sprite
                        selectingSprite = false
                    }
                )
            }
        }
    }

    override fun toJson(): JsonObject = buildJsonObject {
        put("sprite", sprite?.meta?.uid ?: "")
        put("color", buildJsonArray {
            add(color.r)
            add(color.g)
            add(color.b)
            add(color.a)
        })
    }
}