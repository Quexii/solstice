package cc.lapiz.solstice.core.rendering

import org.joml.*

class Camera {
	var position: Vector2f = Vector2f()
		set(value) {
			field.set(value)
			dirty = true
		}

	var zoom: Float = 1f
		set(value) {
			field = value.coerceAtLeast(0.01f)
			dirty = true
		}

	var rotation: Float = 0f
		set(value) {
			field = value
			dirty = true
		}

	private var worldViewMatrix = Matrix4f()
	private var worldProjectionMatrix = Matrix4f()
	private var viewportWidth = 0
	private var viewportHeight = 0
	private var dirty = true

	fun setViewport(width: Int, height: Int) {
		if (viewportWidth != width || viewportHeight != height) {
			viewportWidth = width
			viewportHeight = height
			dirty = true
		}
	}

	private fun updateMatrices() {
		if (!dirty) return

		val aspectRatio = viewportWidth.toFloat() / viewportHeight.toFloat()
		val halfHeight = 1f / zoom
		val halfWidth = halfHeight * aspectRatio

		worldProjectionMatrix.setOrtho(
			-halfWidth, halfWidth,
			-halfHeight, halfHeight,
			-1f, 1f
		)

		worldViewMatrix.identity()
			.translate(-position.x, -position.y, 0f)
			.rotateZ(-rotation)

		dirty = false
	}

	fun getMatrix(): Matrix4f {
		updateMatrices()
		return Matrix4f(worldProjectionMatrix)
			.mul(worldViewMatrix)
	}
	fun screenToWorld(screenX: Float, screenY: Float): Vector3f {
		updateMatrices()

		val normalizedX = (screenX / viewportWidth) * 2f - 1f
		val normalizedY = 1f - (screenY / viewportHeight) * 2f

		val worldPos = Vector3f()
		Matrix4f(worldProjectionMatrix).mul(worldViewMatrix).invert()
			.transformPosition(normalizedX, normalizedY, 0f, worldPos)

		return worldPos
	}

	fun worldToScreen(worldX: Float, worldY: Float): Vector2f {
		updateMatrices()

		val clipPos = Vector3f()
		Matrix4f(worldProjectionMatrix).mul(worldViewMatrix)
			.transformPosition(worldX, worldY, 0f, clipPos)

		val screenX = (clipPos.x + 1f) * 0.5f * viewportWidth
		val screenY = (1f - clipPos.y) * 0.5f * viewportHeight

		return Vector2f(screenX, screenY)
	}
}
