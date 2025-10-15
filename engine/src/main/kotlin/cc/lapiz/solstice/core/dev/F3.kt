package cc.lapiz.solstice.dev

import cc.lapiz.solstice.core.ui.Colors
import cc.lapiz.solstice.data.Color
import cc.lapiz.solstice.input.*
import cc.lapiz.solstice.rendering.*
import cc.lapiz.solstice.rendering.nanovg.*
import cc.lapiz.solstice.ui.*
import cc.lapiz.solstice.window.*
import org.lwjgl.nanovg.*
import org.lwjgl.opengl.GL33C

object F3 {
	private val textsLeft = mutableListOf<String>()
	private val textsRight = mutableListOf<String>()
	private val lines = mutableListOf<Line>()
	private val circles = mutableListOf<Circle>()
	private val textSize = 14f

	fun left(text: String) {
		textsLeft += text
	}

	fun right(text: String) {
		textsRight += text
	}

	fun line(startX: Float, startY: Float, endX: Float, endY: Float, color: Color = Colors.TextPrimary, lineWidth: Float = 1f) {
		lines += Line(startX, startY, endX, endY, color, lineWidth)
	}

	fun circle(x: Float, y: Float, radius: Float, color: Color = Colors.TextPrimary) {
		circles += Circle(x, y, radius, color)
	}

	fun lineWorld(startX: Float, startY: Float, endX: Float, endY: Float, color: Color = Colors.TextPrimary, lineWidth: Float = 1f) {
		val cam = RenderSystem.Camera
		val (sx, sy) = cam.worldToScreen(startX, startY)
		val (ex, ey) = cam.worldToScreen(endX, endY)
		lines += Line(sx, sy, ex, ey, color, lineWidth)
	}

	fun circleWorld(x: Float, y: Float, radius: Float, color: Color = Colors.TextPrimary) {
		val cam = RenderSystem.Camera
		val (sx, sy) = cam.worldToScreen(x, y)
		circles += Circle(sx, sy, radius, color)
	}

	fun render() {
		GL33C.glPolygonMode(GL33C.GL_FRONT_AND_BACK, GL33C.GL_FILL)
		for (line in lines) {
			NVcanvas.strokeLine(line.startX, line.startY, line.endX, line.endY, line.color, line.lineWidth)
		}
		lines.clear()
		for (circle in circles) {
			NVcanvas.circle(circle.x, circle.y, circle.radius, circle.color)
		}
		circles.clear()
		if (textsLeft.isNotEmpty()) {
			textsLeft.forEachIndexed { i, s ->
				NVcanvas.text(6f, 6f + i * textSize, s, Colors.TextPrimary, textSize)
			}
			textsLeft.clear()
		}
		if (textsRight.isNotEmpty()) {
			textsRight.forEachIndexed { i, s ->
				NVcanvas.text(Display.width() - 6f, 6f + i * textSize, s, Colors.TextPrimary, textSize, textAlign = TextAlign.RightTop)
			}
			textsRight.clear()
		}
	}

	fun update() {
		GL33C.glPolygonMode(GL33C.GL_FRONT_AND_BACK, if (Input.isKeyPressed(Keys.F4)) GL33C.GL_LINE else GL33C.GL_FILL)
	}

	private data class Line(val startX: Float, val startY: Float, val endX: Float, val endY: Float, val color: Color = Colors.TextPrimary, val lineWidth: Float = 1f)
	private data class Circle(val x: Float, val y: Float, val radius: Float, val color: Color = Colors.TextPrimary)
}