package cc.lapiz.solstice.core.game.components.impl

import cc.lapiz.solstice.core.game.components.Component
import cc.lapiz.solstice.core.game.components.ImSerialize
import cc.lapiz.solstice.core.game.components.meta.ComponentName
import cc.lapiz.solstice.core.game.components.meta.JsonSerializable
import cc.lapiz.solstice.core.game.components.meta.RequiredComponents
import cc.lapiz.solstice.core.rendering.RenderSystem
import cc.lapiz.solstice.core.rendering.pipeline.mesh.Mesh
import cc.lapiz.solstice.core.rendering.pipeline.shader.UniformScope
import cc.lapiz.solstice.rendering.pipeline.shader.Shader
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject

@ComponentName("Mesh Renderer")
@RequiredComponents(Transform::class)
open class MeshRenderer : Component(), ImSerialize, JsonSerializable {
    var mesh: Mesh? = null
    var shader: Shader? = null
    override fun onRender() {
        assert(mesh != null) { "Mesh is null!" }
        assert(shader != null) { "Shader is null!" }

        preShader()
        RenderSystem.setShader { shader!! }
        RenderSystem.renderMesh(mesh!!, gameObject.getComponent<Transform>()!!.backing) { applyUniforms(this) }
    }

    open fun preShader(): Boolean = true
    open fun applyUniforms(scope: UniformScope) {}
    override fun drawImGui() {}
    override fun toJson(): JsonObject = buildJsonObject { }
}