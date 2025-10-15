package cc.lapiz.solstice.core.game.components.impl

import cc.lapiz.solstice.core.game.components.ImSerialize
import cc.lapiz.solstice.core.game.components.RequireComponent
import cc.lapiz.solstice.game.components.Component
import cc.lapiz.solstice.game.components.impl.Transform
import cc.lapiz.solstice.rendering.RenderSystem
import cc.lapiz.solstice.rendering.pipeline.mesh.Mesh
import cc.lapiz.solstice.rendering.pipeline.shader.Shader
import cc.lapiz.solstice.rendering.pipeline.shader.UniformScope
import cc.lapiz.solstice.ui.imgui.ImGui
import imgui.flag.ImGuiTableFlags

@RequireComponent(Transform::class)
open class MeshRenderer : Component(), ImSerialize {
    var mesh: Mesh? = null
    var shader: Shader? = null
    override fun onRender() {
        assert(mesh != null) { "Mesh is null!" }
        assert(shader != null) { "Shader is null!" }

        preShader()
        RenderSystem.setShader { shader!! }
        RenderSystem.renderMesh(mesh!!, gameObject.getComponent<Transform>()!!.backing) { applyUniforms(this) }
    }

    open fun preShader() {}
    open fun applyUniforms(scope: UniformScope) {}
    open override fun drawImGui() {}
}