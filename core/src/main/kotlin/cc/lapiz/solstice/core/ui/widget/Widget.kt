package cc.lapiz.solstice.core.ui.widget

import cc.lapiz.solstice.core.event.InputEvent

open class Widget(
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float
) {
    var visible: Boolean = true
    val children = mutableListOf<Widget>()

    open fun draw() {
        if (!visible) return
        children.forEach { it.draw() }
    }

    open fun onInput(event: InputEvent): Boolean {
        if (!visible) return false

        for (child in children.reversed()) {
            if (child.onInput(event)) return true
        }
        return false
    }

    fun add(child: Widget) {
        children += child
    }

    fun contains(px: Float, py: Float): Boolean =
        px >= x && py >= y && px < x + width && py < y + height
}