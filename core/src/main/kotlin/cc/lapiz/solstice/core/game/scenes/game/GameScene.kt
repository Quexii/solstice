package cc.lapiz.solstice.core.game.scenes.game

import cc.lapiz.solstice.core.data.*
import cc.lapiz.solstice.core.event.*
import cc.lapiz.solstice.core.game.*
import cc.lapiz.solstice.core.game.ecs.components.*
import cc.lapiz.solstice.core.game.ecs.systems.*
import cc.lapiz.solstice.core.input.*
import cc.lapiz.solstice.core.rendering.*
import cc.lapiz.solstice.core.rendering.nanovg.NVcanvas
import cc.lapiz.solstice.core.rendering.pipeline.shader.*
import cc.lapiz.solstice.core.resource.*
import cc.lapiz.solstice.core.resource.impl.*
import cc.lapiz.solstice.core.ui.Colors
import cc.lapiz.solstice.core.utils.Props
import cc.lapiz.solstice.core.window.Window
import org.joml.*
import kotlin.math.*

class GameScene : Scene() {
	private val selectedEntities = mutableSetOf<Int>()
	private var isDragging = false
	private var dragStartPos: Vector2f? = null
	private var dragEndPos: Vector2f? = null

	override fun onEnter() {
		ecs.registerComponent(Transform::class.java)
		ecs.registerComponent(SpriteRenderer::class.java)
		ecs.registerComponent(CircleRenderer::class.java)
		ecs.registerComponent(Parent::class.java)
		ecs.registerComponent(Children::class.java)
		ecs.registerComponent(BoxCollider::class.java)
		ecs.registerComponent(CircleCollider::class.java)
		ecs.registerComponent(GoToTarget::class.java)

		fun makeUnit(x: Float, y: Float) : Int {
			val unit1 = ecs.createEntity()
			val transform = Transform()
			transform.position.set(x, y)
			transform.scale = Vector2f(8f)

			val circleRenderer = CircleRenderer()
			circleRenderer.shaderColor = floatArrayOf(1f, 0.3f, 0.5f, 0f)

			val spriteRenderer = SpriteRenderer()
			spriteRenderer.shader = ShaderManager.SpriteShader
			spriteRenderer.shaderColor = floatArrayOf(1f, 1f, 1f, 1f)
			spriteRenderer.sprite = ResourceManager.get("base_unit") as SpriteResource

			val collider = CircleCollider()
			collider.radius = 0.5f
			collider.offset = Vector2f(0f, 0f)
			collider.isTrigger = false

			ecs.addComponent(unit1, transform)
			ecs.addComponent(unit1, spriteRenderer)
			ecs.addComponent(unit1, circleRenderer)
			ecs.addComponent(unit1, collider)
			ecs.addComponent(unit1, GoToTarget())

			return unit1
		}

		makeUnit(-16f, 0f)
		makeUnit(-8f, 0f)
		makeUnit(0f, 0f)
		makeUnit(8f, 0f)
		makeUnit(16f, 0f)

		ecs.addSystem(SystemRenderer())
		ecs.addSystem(SystemChildren())
		ecs.addSystem(SystemTargeting())
		super.onEnter()
	}

	override fun update(delta: Float) {
		super.update(delta)

		if (isDragging) {
			val mp = Input.getMousePosition()
			dragEndPos = RenderSystem.Camera.screenToWorld(mp.x, mp.y)
		}
	}

	private fun isEntityInSelectionBox(transform: Transform): Boolean {
		val start = dragStartPos ?: return false
		val end = dragEndPos ?: return false

		val minX = min(start.x, end.x)
		val maxX = max(start.x, end.x)
		val minY = min(start.y, end.y)
		val maxY = max(start.y, end.y)

		val pos = transform.position
		return pos.x >= minX && pos.x <= maxX && pos.y >= minY && pos.y <= maxY
	}

	private fun updateSelectionVisuals() {
		val entities = ecs.query(Transform::class.java, CircleRenderer::class.java)
		for (q in entities) {
			val entity = q.id
			val circleRenderer = q[CircleRenderer::class.java]

			if (selectedEntities.contains(entity)) {
				circleRenderer.shaderColor[3] = 1.0f
			} else {
				circleRenderer.shaderColor[3] = 0f
			}
		}
	}

	override fun onEvent(event: Event) {
		if (event is InputEvent.MousePress && event.button == MouseButton.LEFT) {
			val mp = Input.getMousePosition()
			val worldPos = RenderSystem.Camera.screenToWorld(mp.x, mp.y)

			isDragging = true
			dragStartPos = Vector2f(worldPos)
			dragEndPos = Vector2f(worldPos)

			selectedEntities.clear()
		}

		if (event is InputEvent.MouseRelease && event.button == MouseButton.LEFT) {
			if (isDragging) {
				val entities = ecs.query(Transform::class.java, CircleRenderer::class.java, GoToTarget::class.java)
				for (q in entities) {
					val entity = q.id
					val transform = q[Transform::class.java]

					if (isEntityInSelectionBox(transform)) {
						selectedEntities.add(entity)
					}
				}

				updateSelectionVisuals()

				isDragging = false
				dragStartPos = null
				dragEndPos = null
			}
		}

		if (event is InputEvent.MousePress && event.button == MouseButton.RIGHT) {
			val mp = Input.getMousePosition()
			val worldPos = RenderSystem.Camera.screenToWorld(mp.x, mp.y)

			selectedEntities.forEach { entity ->
				val goToTarget = ecs.getComponent(entity, GoToTarget::class.java)
				if (Input.isKeyPressed(Keys.SHIFT)) {
					goToTarget?.addTarget(Target(0, worldPos))
				} else {
					goToTarget?.targets?.clear()
					goToTarget?.addTarget(Target(0, worldPos))
				}
			}
		}

		super.onEvent(event)
	}

	override fun nanovg() {
		NVcanvas.beginFrame(Window.width().toFloat(), Window.height().toFloat(), 1f)
		super.nanovg()

		if (isDragging && dragStartPos != null && dragEndPos != null) {
			val start = RenderSystem.Camera.worldToScreen(dragStartPos!!.x, dragStartPos!!.y)
			val end = RenderSystem.Camera.worldToScreen(dragEndPos!!.x, dragEndPos!!.y)

			val minX = min(start.x, end.x)
			val maxX = max(start.x, end.x)
			val minY = min(start.y, end.y)
			val maxY = max(start.y, end.y)

			NVcanvas.strokeRect(minX, minY, maxX - minX, maxY - minY, Colors.TextPrimary, 1f)
		}

		selectedEntities.forEach { entity ->
			val transform = ecs.getComponent(entity, Transform::class.java)
			val goToTarget = ecs.getComponent(entity, GoToTarget::class.java)

			if (transform != null && goToTarget != null && goToTarget.targets.isNotEmpty()) {
				val currentPos = transform.position

				val firstTarget = goToTarget.targets.first()
				val firstScreenPos = RenderSystem.Camera.worldToScreen(firstTarget.position.x, firstTarget.position.y)
				val currentScreenPos = RenderSystem.Camera.worldToScreen(currentPos.x, currentPos.y)

				NVcanvas.strokeLine(
					currentScreenPos.x, currentScreenPos.y,
					firstScreenPos.x, firstScreenPos.y,
					Colors.Primary, 2f
				)

				for (i in 0 until goToTarget.targets.size - 1) {
					val current = goToTarget.targets[i]
					val next = goToTarget.targets[i + 1]

					val currentScreen = RenderSystem.Camera.worldToScreen(current.position.x, current.position.y)
					val nextScreen = RenderSystem.Camera.worldToScreen(next.position.x, next.position.y)

					NVcanvas.strokeLine(
						currentScreen.x, currentScreen.y,
						nextScreen.x, nextScreen.y, Colors.PrimaryActive, 2f
					)
				}

				goToTarget.targets.forEach { target ->
					val screenPos = RenderSystem.Camera.worldToScreen(target.position.x, target.position.y)
					NVcanvas.circle(screenPos.x, screenPos.y, 4f, Colors.PrimaryActive)
				}
			}
		}

		ecs.allEntities().forEach { entity ->
			val transform = ecs.getComponent(entity, Transform::class.java)

			if (transform != null) {
				val pos = RenderSystem.Camera.worldToScreen(transform.position.x, transform.position.y)
				if (Props.DEBUG) {
					val isSelected = selectedEntities.contains(entity)
					NVcanvas.text(pos.x, pos.y, "Entity #$entity" + if (isSelected) " (selected)" else "", Colors.TextPrimary, 12f)
					NVcanvas.text(pos.x, pos.y + 12, "x: ${pos.x}, y: ${pos.y}", Colors.TextPrimary, 12f)
					ecs.allComponents(entity).forEachIndexed { i, entity ->
						NVcanvas.text(pos.x, pos.y + 24 + i * 12, "#$i: ${entity::class.java.simpleName} $entity", Colors.TextPrimary, 12f)
					}
				}
			}
		}
		NVcanvas.endFrame()
	}

}
