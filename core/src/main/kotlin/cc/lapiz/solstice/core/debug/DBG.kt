package cc.lapiz.solstice.core.debug

import cc.lapiz.solstice.core.input.*
import cc.lapiz.solstice.core.rendering.nanovg.*
import cc.lapiz.solstice.core.rendering.platform.Graphics
import cc.lapiz.solstice.core.ui.*
import cc.lapiz.solstice.core.window.*

object DBG {
	private val textsLeft = mutableListOf<String>()
	private val textsRight = mutableListOf<String>()
	private val textSize = 14f

	fun left(text: String) {
		textsLeft += text
	}

	fun right(text: String) {
		textsRight += text
	}

	fun renderTexts() {
		Graphics.polygonMode(Graphics.FRONT_AND_BACK, Graphics.FILL)
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
}