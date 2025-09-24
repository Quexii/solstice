package cc.lapiz.solstice.core.ui.elements

import cc.lapiz.solstice.core.input.Input
import cc.lapiz.solstice.core.rendering.nanovg.NVcanvas
import cc.lapiz.solstice.core.ui.*
import cc.lapiz.solstice.core.ui.action.*
import cc.lapiz.solstice.core.ui.animate.Easing
import cc.lapiz.solstice.core.ui.animate.lerpColor
import cc.lapiz.solstice.core.ui.layout.*
import cc.lapiz.solstice.core.ui.style.*

fun UIElement.center(attrib: Attrib, block: UIStack.() -> Unit) = stack(attrib, flex = Flex.Center, align = Align.Center, init = block)

fun UIElement.button(text: String, onClick: () -> Unit) = center(
	Attrib.size(200.px, 50.px)
) {
	val animatedColor = animatedProperty("buttonBg", Colors.Panel, Colors.Border, 0.1f) { from, to, t ->
		lerpColor(from, to, t, NVcanvas.RGBAf(from.r(), from.g(), from.b(), from.a()))
	}

	center(Attrib.size(200.px, 50.px).fill(animatedColor).stroke(Colors.Border, 2.px)) {
		label(Attrib, text)
	}.onHover { _,it -> animatedColor.setState(it) }
}.onClick { onClick() }

fun UIElement.slider(text: String, supplier: () -> Float, onChange: (Float) -> Unit = {}) = stack(Attrib.size(200.px, 30.px), direction = Direction.Column) {
	var dragValue by rememberState("slider_value", supplier())
	var dragging by rememberState("slider_dragging", false)
	val lb = label(Attrib, text.format(supplier()), fontSize = 16f)
	stack(Attrib.size(200.px, 10.px).fill(Colors.Background), direction = Direction.Column) {
		stack(Attrib.fullSize(heightRatio = 1.px).fill(Colors.Primary), direction = Direction.Row, flex = Flex.End) {
			layoutIf { store ->
				if (store.get() != dragValue) {
					properties.size = properties.size.copy(width = dragValue * (parent!!.properties.size.width))
					onChange(dragValue)
					lb.text = text.format(dragValue)
					store.set(dragValue)
					true
				} else false
			}
		}
	}
		.onMousePress {
			dragging = true
			val localX = Input.getMousePosition().x - (it.properties.position.x + it.properties.size.width * 0.5f)
			dragValue = (localX / it.properties.size.width + 0.5f).coerceIn(0f, 1f)
		}
		.onMouseRelease {
			dragging = false
			val localX = Input.getMousePosition().x - (it.properties.position.x + it.properties.size.width * 0.5f)
			dragValue = (localX / it.properties.size.width + 0.5f).coerceIn(0f, 1f)
		}
		.onMouseMove { ele, x, _ ->
			if (dragging) {
				val localX = x - (ele.properties.position.x + ele.properties.size.width * 0.5f)
				dragValue = (localX / ele.properties.size.width + 0.5f).coerceIn(0f, 1f)
			}
		}
}