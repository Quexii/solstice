package cc.lapiz.solstice.game.scene

import cc.lapiz.solstice.core.game.Scene
import cc.lapiz.solstice.core.game.components.impl.SpriteRenderer
import cc.lapiz.solstice.game.*
import cc.lapiz.solstice.core.game.components.impl.Transform
import cc.lapiz.solstice.core.rendering.pipeline.mesh.Meshes
import cc.lapiz.solstice.core.rendering.pipeline.shader.ShaderManager

class MainScene : Scene() {
	override fun onEnter() {
		createObject {
			val transform = addComponent<Transform>()
			transform.scale.set(4f, 4f)

			val spriteRenderer = addComponent<SpriteRenderer>()
//			spriteRenderer.sprite = ResourceManager.get("test")
			spriteRenderer.mesh = Meshes.UVRect
			spriteRenderer.shader = ShaderManager.SpriteShader
		}
	}
}