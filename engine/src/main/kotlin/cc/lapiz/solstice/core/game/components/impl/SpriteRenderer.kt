package cc.lapiz.solstice.core.game.components.impl

import cc.lapiz.solstice.rendering.pipeline.shader.UniformScope
import cc.lapiz.solstice.rendering.texture.TextureUtil
import cc.lapiz.solstice.resource.impl.SpriteId
import cc.lapiz.solstice.core.ui.Colors
import cc.lapiz.solstice.ui.imgui.ImGui
import imgui.flag.ImGuiTableFlags

class SpriteRenderer : MeshRenderer() {
    var sprite: SpriteId? = null
    var color = Colors.White
    private val colorArray = floatArrayOf(color.r, color.g, color.b, color.a)

    override fun preShader() {
        TextureUtil.bindTexture(sprite!!.textureId())
    }

    override fun applyUniforms(scope: UniformScope) {
        assert(sprite != null) { "Sprite is null!" }

        scope.sampler2D("Texture", 0)
        scope.vec4("Color", color.r, color.g, color.b, color.a)
    }

    override fun drawImGui() {
        ImGui.beginTable("##spriteRenderer", 2, ImGuiTableFlags.SizingStretchSame)
        val size = imgui.ImGui.getContentRegionAvail()

        ImGui.tableNextRow()
        val fieldWidth = (size.x + 24) / 3f
        ImGui.tableSetColumnIndex(0)
        imgui.ImGui.pushItemWidth(fieldWidth)
        ImGui.text("Color")
        ImGui.tableSetColumnIndex(1)
        if (ImGui.colorEdit4("##spriteRenderer##colorEdit4", colorArray)) {
            color.set(colorArray[0], colorArray[1], colorArray[2], colorArray[3])
        }
        ImGui.endTable()
    }
}