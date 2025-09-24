package cc.lapiz.solstice.core.ui

import cc.lapiz.solstice.core.event.*
import cc.lapiz.solstice.core.ui.elements.*
import cc.lapiz.solstice.core.ui.properties.*
import cc.lapiz.solstice.core.ui.react.UIState

class UI() {
	private var content: UIElement? = null

	fun setContent(element: UIElement) {
		content = element
		element.layoutRecursively()
	}

	private fun collectAllElements(element: UIElement, list: MutableList<UIElement>) {
		list.add(element)
		element.children.forEach { child ->
			collectAllElements(child, list)
		}
	}

	fun update(delta: Float) {
		content?.update(delta)
	}

	fun render() {
		content?.render()
	}

	fun onEvent(event: Event) {
		content?.onEvent(event)
	}

	fun resize(width: Float, height: Float) {
		val newSize = Size(width, height)
		content?.properties?.size = newSize
	}
}