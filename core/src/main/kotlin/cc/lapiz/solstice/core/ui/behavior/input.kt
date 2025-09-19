package cc.lapiz.solstice.core.ui.behavior

import cc.lapiz.solstice.core.event.*

abstract class InputUIBehavior(override val func: () -> Unit) : UIBehavior {
	class MousePress(func: () -> Unit) : InputUIBehavior(func) {
		override fun onEvent(event: Event) {
			if (event is InputEvent.MousePress) func()
		}
	}

	class MouseRelease(func: () -> Unit) : InputUIBehavior(func) {
		override fun onEvent(event: Event) {
			if (event is InputEvent.MouseRelease) func()
		}
	}

	class MouseMove(func: () -> Unit) : InputUIBehavior(func) {
		override fun onEvent(event: Event) {
			if (event is InputEvent.MouseMove) func()
		}
	}

	class KeyPress(func: () -> Unit) : InputUIBehavior(func) {
		override fun onEvent(event: Event) {
			if (event is InputEvent.KeyPress) func()
		}
	}

	class KeyRelease(func: () -> Unit) : InputUIBehavior(func) {
		override fun onEvent(event: Event) {
			if (event is InputEvent.KeyRelease) func()
		}
	}

	class CharInput(func: () -> Unit) : InputUIBehavior(func) {
		override fun onEvent(event: Event) {
			if (event is InputEvent.MouseScroll) func()
		}
	}
}