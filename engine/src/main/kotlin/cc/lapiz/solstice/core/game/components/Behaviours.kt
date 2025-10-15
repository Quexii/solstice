package cc.lapiz.solstice.game.components

import cc.lapiz.solstice.event.Event

interface Behaviours {
    fun onStart() {}
    fun onUpdate(delta: Float) {}
    fun onRender() {}
    fun onEvent(event: Event) {}
}