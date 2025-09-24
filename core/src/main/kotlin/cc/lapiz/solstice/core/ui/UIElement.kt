package cc.lapiz.solstice.core.ui

import cc.lapiz.solstice.core.data.Rect
import cc.lapiz.solstice.core.data.Store
import cc.lapiz.solstice.core.event.Event
import cc.lapiz.solstice.core.event.InputEvent
import cc.lapiz.solstice.core.ui.action.Action
import cc.lapiz.solstice.core.ui.action.ActionHandler
import cc.lapiz.solstice.core.ui.animate.AnimatedProperty
import cc.lapiz.solstice.core.ui.elements.UIStack
import cc.lapiz.solstice.core.ui.properties.Properties
import cc.lapiz.solstice.core.ui.react.UIState
import cc.lapiz.solstice.core.ui.style.Attrib
import cc.lapiz.solstice.core.ui.style.CombinedAttrib
import cc.lapiz.solstice.core.ui.style.DrawAttrib
import cc.lapiz.solstice.core.ui.style.LayoutAttrib
import cc.lapiz.solstice.core.ui.style.Style
import cc.lapiz.solstice.core.utils.contains
import kotlin.properties.ReadWriteProperty

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

	var requestLayout: Boolean = false
	private val requestLayoutStore = Store(null)
	var requestLayoutCallback : ((Store) -> Boolean)? = null

	private var pressed: Boolean = false
	val actionQueue = ArrayDeque<Action>()
	val actonHandlers = ActionHandler()
	val attributes = ArrayList<Attrib>()

	val children = ArrayList<UIElement>()

	val properties = Properties()
	val state = UIState()

	val bounds get() = Rect(properties.position.x, properties.position.y, properties.size.width, properties.size.height)
	private val animatedProperties = mutableListOf<AnimatedProperty<*>>()

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
		if (!enabled) return

		animatedProperties.forEach { it.update(delta) }

		if (requestLayoutCallback?.invoke(requestLayoutStore) == true) {
			requestLayout = true
		}

		if (requestLayout) {
			layoutRecursively()
			requestLayout = false
		}

		while (actionQueue.isNotEmpty()) {
			actonHandlers.trigger(actionQueue.removeFirst(), this)
		}

		for (child in children) {
			child.update(delta)
		}
	}

	abstract fun draw()

	fun render() {
		if (visible) {
			attributes.filterIsInstance<DrawAttrib>().forEach { it.apply(this) }
			draw()
		}

		for (child in children) {
			child.render()
		}
	}

	fun layoutIfNeeded() {
		attributes.filterIsInstance<LayoutAttrib>().forEach { it.apply(this) }
		(this as? UIStack)?.updateLayout()
		children.forEach { (it as? UIStack)?.layoutIfNeeded() }
	}

	fun layoutRecursively() {
		layoutIfNeeded()
		children.forEach { child ->
			child.layoutRecursively()
		}
	}

	fun addAnimatedProperty(property: AnimatedProperty<*>) {
		animatedProperties.add(property)
	}

	open fun onEvent(event: Event) {
		if (event is InputEvent) {
			if (event is InputEvent.MouseMove) {
				actionQueue.add(Action.MouseMove)
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
					actionQueue.add(Action.MousePress)
				}
			}

			if (event is InputEvent.MouseRelease) {
				if (pressed && hovered) {
					actionQueue.add(Action.Click)
					pressed = false
				}
				pressed = false
				actionQueue.add(Action.MouseRelease)
			}
		}

		for (child in children) {
			child.onEvent(event)
		}
	}
}

fun UIElement.addChild(element: UIElement) {
	element.parent = this
	element.depth = this.depth + 1
	children.add(element)
}


internal inline fun <reified T> UIElement.rememberState(key: String, initialValue: T): ReadWriteProperty<Any?, T> {
	return state.remember(key, initialValue)
}

internal inline fun <reified T> UIElement.rememberObservableState(key: String, initialValue: T, noinline onChange: (T) -> Unit = {}): ReadWriteProperty<Any?, T> {
	return state.rememberObservable(key, initialValue, onChange)
}

fun <T> UIElement.animatedProperty(
	key: String,
	falseValue: T,
	trueValue: T,
	duration: Float = 0.3f,
	easing: (Float) -> Float = { it },
	lerp: (T, T, Float) -> T,
): AnimatedProperty<T> {
	val animatedProp = AnimatedProperty(
		this, key, falseValue, trueValue, duration, easing, lerp
	)
	addAnimatedProperty(animatedProp)
	return animatedProp
}

fun UIElement.layoutIf(callback: (Store) -> Boolean) {
	requestLayoutCallback = callback
}