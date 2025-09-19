package cc.lapiz.solstice.core.ui.widget

import cc.lapiz.solstice.core.event.*
import cc.lapiz.solstice.core.input.*
import cc.lapiz.solstice.core.ui.behavior.*
import cc.lapiz.solstice.core.ui.style.*
import cc.lapiz.solstice.core.utils.*

abstract class Widget(val modifier: Modifier, val alignment: Alignment, val arrangement: Arrangement, var padding: Padding) {
	val children = mutableListOf<Widget>()
	val behaviors = mutableListOf<UIBehavior>()
	var parent: Widget? = null
	private var dirty = true

	var x: Float = 0f
	var y: Float = 0f
	var width: Float = 0f
	var height: Float = 0f

	var focused = false

	open fun draw() {
		if (dirty) {
			onDirty()
			dirty = false
		}

		drawModifiers(modifier)

//		NVcanvas.strokeRect(x, y, width, height, Colors.TextPrimary, 1f)
//		NVcanvas.text(x, y, "pr: $parent", Colors.TextPrimary, size = 10f)
//		NVcanvas.text(x, y + 10, "x: $x y: $y", Colors.TextPrimary, size = 10f)
//		NVcanvas.text(x, y + 20, "width: $width height: $height", Colors.TextPrimary, size = 10f)

		for (child in children) {
			child.draw()
		}
	}

	fun addChild(child: Widget) {
		children.add(child)
		child.parent = this
		markDirty()
	}

	open fun onDirty() {
		behaviors.clear()
		dirtyModifiers(modifier)
		rearrange()
	}

	private fun drawModifiers(modifier: Modifier) {
		when (modifier) {
			is CombinedModifier -> {
				drawModifiers(modifier.first)
				drawModifiers(modifier.second)
			}
			is FillModifier -> applyFill(modifier)
			is StrokeModifier -> applyStroke(modifier)
			is TextModifier -> applyText(modifier)
			else -> {}
		}
	}

	private fun dirtyModifiers(modifier: Modifier) {
		when (modifier) {
			is CombinedModifier -> {
				dirtyModifiers(modifier.first)
				dirtyModifiers(modifier.second)
			}
			is FillModifier -> applyFill(modifier)
			is StrokeModifier -> applyStroke(modifier)
			is SizeModifier -> applySize(modifier)
			is WidthModifier -> applyWidth(modifier)
			is HeightModifier -> applyHeight(modifier)
			is MaxSizeModifier -> applyMaxSize()
			is MaxWidthModifier -> applyMaxWidth()
			is MaxHeightModifier -> applyMaxHeight()
			is PaddingModifier -> applyPadding(modifier)
			is ClickableModifier -> applyClickable(modifier)
			else -> {}
		}

		for (child in children) {
			child.dirtyModifiers(child.modifier)
		}
	}

	fun rearrange() {
		val availableWidth = width - padding.left - padding.right
		val availableHeight = height - padding.top - padding.bottom

		arrangement.arrange(children, availableWidth, availableHeight)

		alignment.align(children, availableWidth, availableHeight)

		for (child in children) {
			child.x += x + padding.left
			child.y += y + padding.top
			child.rearrange()
		}
	}

	fun markDirty() {
		dirty = true
		children.forEach { child ->
			child.markDirty()
		}
	}

	open fun onEvent(event: Event) {
		val mp = Input.getMousePosition()
		behaviors.forEach {
			if ((isMouseEvent(event) && contains(mp.x, mp.y, x, y, width, height)) || (isKeyEvent(event) && focused)) {
				it.onEvent(event)
			}
		}

		for (child in children) {
			child.onEvent(event)
		}
	}

	open fun resize(width: Float, height: Float) {
		this.width = width
		this.height = height
		markDirty()
	}
}
