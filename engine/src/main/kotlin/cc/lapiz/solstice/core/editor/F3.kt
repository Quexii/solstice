package cc.lapiz.solstice.core.editor

import cc.lapiz.solstice.core.data.Color
import cc.lapiz.solstice.core.input.Input
import cc.lapiz.solstice.core.input.Keys
import cc.lapiz.solstice.core.rendering.RenderSystem
import cc.lapiz.solstice.core.ui.Colors
import cc.lapiz.solstice.core.ui.imgui.ImGuiCtx
import cc.lapiz.solstice.core.utils.argbToAbgr
import cc.lapiz.solstice.core.window.Display
import imgui.ImGui
import org.lwjgl.opengl.GL33C

object F3 {
	private val textsLeft = mutableListOf<String>()
	private val textsRight = mutableListOf<String>()
	private val lines = mutableListOf<Line>()
	private val circles = mutableListOf<Circle>()

	fun left(text: String) {
		textsLeft += text
	}

	fun right(text: String) {
		textsRight += text
	}

	fun line(
		startX: Float,
		startY: Float,
		endX: Float,
		endY: Float,
		color: Color = Colors.TextPrimary,
		lineWidth: Float = 1f
	) {
		lines += Line(startX, startY, endX, endY, color, lineWidth)
	}

	fun circle(x: Float, y: Float, radius: Float, color: Color = Colors.TextPrimary) {
		circles += Circle(x, y, radius, color)
	}

	fun lineWorld(
		startX: Float,
		startY: Float,
		endX: Float,
		endY: Float,
		color: Color = Colors.TextPrimary,
		lineWidth: Float = 1f
	) {
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
			ImGui.getBackgroundDrawList().addLine(
				line.startX,
				line.startY,
				line.endX,
				line.endY,
				line.color.toInt().argbToAbgr(),
				line.lineWidth
			)
		}
		lines.clear()
		for (circle in circles) {
			ImGui.getBackgroundDrawList()
				.addCircle(circle.x, circle.y, circle.radius, circle.color.toInt().argbToAbgr())
		}
		circles.clear()
		if (textsLeft.isNotEmpty()) {
			textsLeft.forEachIndexed { i, s ->
				ImGui.getBackgroundDrawList().addText(6f, 6f + i * 16f, -1, s)
			}
			textsLeft.clear()
		}
		if (textsRight.isNotEmpty()) {
			textsRight.forEachIndexed { i, s ->
				ImGui.getBackgroundDrawList().addText(
					Display.width() - 6f - ImGuiCtx.io().fontDefault.calcTextSizeA(16f, 1000f, 1000f, s).x,
					6f + i * 16f,
					-1,
					s
				)
			}
			textsRight.clear()
		}
	}

	fun update() {
		GL33C.glPolygonMode(GL33C.GL_FRONT_AND_BACK, if (Input.isKeyPressed(Keys.F4)) GL33C.GL_LINE else GL33C.GL_FILL)
	}

	private data class Line(
		val startX: Float,
		val startY: Float,
		val endX: Float,
		val endY: Float,
		val color: Color = Colors.TextPrimary,
		val lineWidth: Float = 1f
	)

	private data class Circle(val x: Float, val y: Float, val radius: Float, val color: Color = Colors.TextPrimary)
}