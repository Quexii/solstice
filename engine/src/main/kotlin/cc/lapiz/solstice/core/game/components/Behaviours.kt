package cc.lapiz.solstice.core.game.components

import cc.lapiz.solstice.core.event.Event

interface Behaviours {
    fun onStart() {}
    fun onUpdate(delta: Float) {}
    fun onRender() {}
    fun onEvent(event: Event) {}
}