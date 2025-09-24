package cc.lapiz.solstice.core.ui.action

import cc.lapiz.solstice.core.event.Event

interface Action {
	object Click: Action
	object MousePress: Action
	object MouseRelease: Action
	object MouseMove: Action
	object HoverEnter: Action
	object HoverExit: Action
}