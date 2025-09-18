package cc.lapiz.solstice.core.ui.widget

import cc.lapiz.solstice.core.event.InputEvent
import cc.lapiz.solstice.core.input.Input
import cc.lapiz.solstice.core.input.MouseButton
import cc.lapiz.solstice.core.rendering.RenderSystem
import cc.lapiz.solstice.core.rendering.nanovg.Align
import cc.lapiz.solstice.core.rendering.nanovg.NVcanvas
import cc.lapiz.solstice.core.ui.Colors

class Button(val label: String, x: Float, y: Float, width: Float, height: Float, val onClick: () -> Unit) : Widget(x, y, width, height) {
	override fun draw() {
		NVcanvas.apply {
			rect(x, y, width, height, Colors.Panel)
			text(x + width / 2, y + height / 2, label, Colors.Primary, RenderSystem.DefaultFont.Default, align = Align.CenterMiddle)
		}
		super.draw()
	}

	override fun onInput(event: InputEvent): Boolean {
		val mp = Input.getMousePosition()
		if (contains(mp.x, mp.y) && event is InputEvent.MousePress && event.button == MouseButton.LEFT) {
			onClick()
			return true
		}
		return super.onInput(event)
	}
}