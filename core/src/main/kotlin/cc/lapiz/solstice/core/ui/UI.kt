package cc.lapiz.solstice.core.ui

import cc.lapiz.solstice.core.event.*
import cc.lapiz.solstice.core.ui.elements.*
import cc.lapiz.solstice.core.ui.properties.*

class UI() {
	private var content: UIElement? = null

	fun setContent(element: UIElement) {
		content = element
		layoutRecursively(element)
	}

	private fun collectAllElements(element: UIElement, list: MutableList<UIElement>) {
		list.add(element)
		element.children.forEach { child ->
			collectAllElements(child, list)
		}
	}

	private fun layoutRecursively(element: UIElement) {
		if (element is UIStack) {
			element.layoutIfNeeded()
		}
		element.children.forEach { child ->
			layoutRecursively(child)
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