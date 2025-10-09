package cc.lapiz.solstice.core.dev

import cc.lapiz.solstice.core.input.*
import cc.lapiz.solstice.core.rendering.*
import cc.lapiz.solstice.core.rendering.nanovg.*
import cc.lapiz.solstice.core.rendering.platform.*
import cc.lapiz.solstice.core.ui.*
import cc.lapiz.solstice.core.window.*
import org.lwjgl.nanovg.*

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

	fun line(startX: Float, startY: Float, endX: Float, endY: Float, color: NVGColor = Colors.TextPrimary, lineWidth: Float = 1f) {
		lines += Line(startX, startY, endX, endY, color, lineWidth)
	}

	fun circle(x: Float, y: Float, radius: Float, color: NVGColor = Colors.TextPrimary) {
		circles += Circle(x, y, radius, color)
	}

	fun lineWorld(startX: Float, startY: Float, endX: Float, endY: Float, color: NVGColor = Colors.TextPrimary, lineWidth: Float = 1f) {
		val cam = RenderSystem.Camera
		val (sx, sy) = cam.worldToScreen(startX, startY)
		val (ex, ey) = cam.worldToScreen(endX, endY)
		lines += Line(sx, sy, ex, ey, color, lineWidth)
	}

	fun circleWorld(x: Float, y: Float, radius: Float, color: NVGColor = Colors.TextPrimary) {
		val cam = RenderSystem.Camera
		val (sx, sy) = cam.worldToScreen(x, y)
		circles += Circle(sx, sy, radius, color)
	}

	fun render() {
		Graphics.polygonMode(Graphics.FRONT_AND_BACK, Graphics.FILL)
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
				NVcanvas.text(Window.width() - 6f, 6f + i * textSize, s, Colors.TextPrimary, textSize, textAlign = TextAlign.RightTop)
			}
			textsRight.clear()
		}
	}

	fun update() {
		Graphics.polygonMode(Graphics.FRONT_AND_BACK, if (Input.isKeyPressed(Keys.F4)) Graphics.LINE else Graphics.FILL)
	}

	private data class Line(val startX: Float, val startY: Float, val endX: Float, val endY: Float, val color: NVGColor = Colors.TextPrimary, val lineWidth: Float = 1f)
	private data class Circle(val x: Float, val y: Float, val radius: Float, val color: NVGColor = Colors.TextPrimary)
}