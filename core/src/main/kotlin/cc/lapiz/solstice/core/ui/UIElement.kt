package cc.lapiz.solstice.core.ui

import cc.lapiz.solstice.core.data.Rect
import cc.lapiz.solstice.core.event.Event
import cc.lapiz.solstice.core.event.InputEvent
import cc.lapiz.solstice.core.ui.action.Action
import cc.lapiz.solstice.core.ui.action.ActionHandler
import cc.lapiz.solstice.core.ui.elements.UIStack
import cc.lapiz.solstice.core.ui.properties.Properties
import cc.lapiz.solstice.core.ui.style.Attrib
import cc.lapiz.solstice.core.ui.style.CombinedAttrib
import cc.lapiz.solstice.core.ui.style.DrawAttrib
import cc.lapiz.solstice.core.ui.style.Style
import cc.lapiz.solstice.core.utils.contains

abstract class UIElement(var attrib: Attrib) {
	var parent: UIElement? = null
	var key: String? = null
	var depth = 0

	var enabled: Boolean = true
	var visible: Boolean = true

	var focused: Boolean = false
		private set
	var hovered: Boolean = false
		private set

	private var pressed: Boolean = false
	val actionQueue = ArrayDeque<Action>()
	val actonHandlers = ActionHandler()
	val attributes = ArrayList<Attrib>()
	val modifierRequests = ArrayList<() -> Unit>()

	val children = ArrayList<UIElement>()

	val properties = Properties()

	val bounds get() = Rect(properties.position.x, properties.position.y, properties.size.width, properties.size.height)

	init {
		while (attrib is CombinedAttrib) {
			attributes.add((attrib as CombinedAttrib).second)
			attrib = (attrib as CombinedAttrib).first
		}

		attributes.reverse()
		attributes.forEach { it.apply(this) }

		println("Created $this with attributes: ${attributes.joinToString { it.javaClass.simpleName } }")
	}

	open fun update(delta: Float) {
		while (actionQueue.isNotEmpty()) {
			actonHandlers.trigger(actionQueue.removeFirst())
		}

		for (child in children) {
			child.update(delta)
		}
	}

	abstract fun draw()

	fun render() {
		if (visible) {
			attributes.filter { it is DrawAttrib }.forEach { it.apply(this) }
			draw()
		}

		for (child in children) {
			child.render()
		}
	}

	open fun onEvent(event: Event) {
		if (event is InputEvent) {
			if (event is InputEvent.MouseMove) {
				if (contains(event.position.x, event.position.y, properties.position.x, properties.position.y, properties.size.width, properties.size.height)) {
					if (!hovered) {
						hovered = true
						actionQueue.add(Action.HoverEnter)
					}
				} else {
					if (hovered) {
						hovered = false
						actionQueue.add(Action.HoverExit)
					}
				}
			}

			if (event is InputEvent.MousePress) {
				if (hovered) {
					pressed = true
				}
			}

			if (event is InputEvent.MouseRelease) {
				if (pressed && hovered) {
					actionQueue.add(Action.Click)
					pressed = false
				}
				pressed = false
			}
		}

		for (child in children) {
			child.onEvent(event)
		}
	}

	fun processModifiers() {
		println("$this - ${modifierRequests.size} - $key - $depth")
		println("properties before: ${properties.position}, ${properties.size}")
		if (modifierRequests.isNotEmpty()) {
			modifierRequests.forEach { it() }
			modifierRequests.clear()
		}
		println("properties after: ${properties.position}, ${properties.size}")
		println()
	}
}

fun UIElement.addChild(element: UIElement) {
	element.parent = this
	element.depth = this.depth + 1
	children.add(element)
}