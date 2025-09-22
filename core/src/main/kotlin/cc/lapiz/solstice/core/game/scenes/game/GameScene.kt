package cc.lapiz.solstice.core.game.scenes.game

import cc.lapiz.solstice.core.data.*
import cc.lapiz.solstice.core.game.*
import cc.lapiz.solstice.core.game.ecs.components.*
import cc.lapiz.solstice.core.game.ecs.systems.*
import cc.lapiz.solstice.core.rendering.pipeline.shader.ShaderManager
import cc.lapiz.solstice.core.resource.ResourceManager
import cc.lapiz.solstice.core.resource.impl.SpriteResource
import org.joml.*

class GameScene : Scene() {
	override fun onEnter() {
		val unit1 = ecs.createEntity()
		ecs.registerComponent(Transform::class.java)
		ecs.registerComponent(SpriteRenderer::class.java)

		val transform = Transform()
		transform.scale = Vector2f(8f)

		val spriteRenderer = SpriteRenderer()
		spriteRenderer.shader = ShaderManager.SpriteShader
		spriteRenderer.shaderColor = floatArrayOf(1f, 1f, 1f, 1f)
		spriteRenderer.sprite = ResourceManager.get("base_unit") as SpriteResource

		ecs.addComponent(unit1, transform)
		ecs.addComponent(unit1, spriteRenderer)

		ecs.addSystem(SystemRenderer())
		super.onEnter()
	}

	override fun update(delta: Float) {
		super.update(delta)

		ecs.getComponent(0, Transform::class.java)?.rotation = (System.currentTimeMillis() % 3600) / 10f
	}
}