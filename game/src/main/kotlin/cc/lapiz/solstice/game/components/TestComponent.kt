package cc.lapiz.solstice.game.components

import cc.lapiz.solstice.core.game.components.Component
import cc.lapiz.solstice.core.game.components.ImSerialize
import cc.lapiz.solstice.core.game.components.impl.Transform
import cc.lapiz.solstice.core.game.components.meta.ComponentName
import cc.lapiz.solstice.core.game.components.meta.RequiredComponents
import cc.lapiz.solstice.core.input.Input
import cc.lapiz.solstice.core.rendering.Camera
import cc.lapiz.solstice.core.rendering.RenderSystem
import imgui.ImGui

@ComponentName("Test Component")
@RequiredComponents(Transform::class)
class TestComponent: Component(), ImSerialize {

    override fun onUpdate(delta: Float) {
        val mp = RenderSystem.Camera.screenToWorld(Input.getMousePosition().x, Input.getMousePosition().y)
        gameObject.getComponent<Transform>()?.position?.set(mp.x, mp.y)
    }
    override fun drawImGui() {}
}