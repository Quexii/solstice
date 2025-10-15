package cc.lapiz.solstice.game.scene

import cc.lapiz.solstice.core.game.components.impl.SpriteRenderer
import cc.lapiz.solstice.data.Vector2
import cc.lapiz.solstice.game.*
import cc.lapiz.solstice.game.components.impl.Transform
import cc.lapiz.solstice.rendering.pipeline.mesh.Meshes
import cc.lapiz.solstice.rendering.pipeline.shader.ShaderManager
import cc.lapiz.solstice.resource.ResourceManager

class MainScene: Scene() {
	override fun onEnter() {
		createObject {
			val transform = addComponent<Transform>()
			transform.scale.set(4f, 4f)

			val spriteRenderer = addComponent<SpriteRenderer>()
			spriteRenderer.sprite = ResourceManager.get("test")
			spriteRenderer.mesh = Meshes.UVRect
			spriteRenderer.shader = ShaderManager.SpriteShader
		}
	}
}