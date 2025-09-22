package cc.lapiz.solstice.core.rendering

import cc.lapiz.solstice.core.window.Window
import org.joml.*
import org.lwjgl.BufferUtils
import java.nio.FloatBuffer

class Camera {
	private var position = Vector2f(0f, 0f)
	private var rotation: Float = 0f
	private var zoom: Float = 10f
	private val projectionMatrix = Matrix4f()
	var projectionBuffer: FloatBuffer = BufferUtils.createFloatBuffer(16)

	fun projectWorld() {
		val width = Window.width()
		val height = Window.height()

		val aspectRatio = width.toFloat() / height
		val halfWidth = width / 2f / zoom
		val halfHeight = height / 2f / zoom
		projectionMatrix.identity().ortho(
			-halfWidth * aspectRatio,
			halfWidth * aspectRatio,
			-halfHeight * aspectRatio,
			halfHeight * aspectRatio,
			-1f,
			1f
		).translate(position.x, position.y, 0f).rotateZ(rotation)
		projectionMatrix.get(projectionBuffer)
	}

	fun projectHUD() {
		val width = Window.width()
		val height = Window.height()

		projectionMatrix.identity().ortho(
			0f,
			width.toFloat(),
			height.toFloat(),
			0f,
			-1f,
			1f
		)
		projectionMatrix.get(projectionBuffer)
	}

	fun screenToWorld(sx: Float, sy: Float): Vector2f {
		val ndcX = (2f * sx) / Window.width() - 1f
		val ndcY = 1f - (2f * sy) / Window.height()
		val clipCoords = Vector4f(ndcX, ndcY, -1f, 1f)

		val invProj = Matrix4f(projectionMatrix).invert()
		val eyeCoords = invProj.transform(clipCoords)
		eyeCoords.z = -1f
		eyeCoords.w = 0f

		val invView = Matrix4f().identity().rotateZ(-rotation).translate(-position.x, -position.y, 0f).invert()
		val worldCoords = invView.transform(eyeCoords)

		return Vector2f(worldCoords.x, worldCoords.y)
	}

	fun rotateBy(angle: Float) {
		rotation += angle
	}

	fun rotateTo(angle: Float) {
		rotation = angle
	}

	fun moveTo(x: Float, y: Float) {
		position.set(x, y)
	}

	fun moveBy(dx: Float, dy: Float) {
		position.add(dx, dy)
	}

	fun zoomBy(amount: Float) {
		zoom += amount
		zoom = zoom.coerceIn(1f, 30f)
	}

	fun setZoom(amount: Float) {
		zoom = amount
	}

	fun getProjectionMatrix() = projectionMatrix
}