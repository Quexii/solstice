package cc.lapiz.solstice.core.ui.elements

import cc.lapiz.solstice.core.rendering.nanovg.*
import cc.lapiz.solstice.core.ui.*
import cc.lapiz.solstice.core.ui.layout.*
import cc.lapiz.solstice.core.ui.properties.*
import cc.lapiz.solstice.core.ui.style.*
import cc.lapiz.solstice.core.utils.Props

class UIStack(attrib: Attrib, val spacing: Float, val direction: Direction, val flex: Flex, val align: Align) : UIElement(attrib) {
	fun updateLayout() {
		if (children.isEmpty()) return

		val isVertical = direction == Direction.Column || direction == Direction.ColumnReverse
		val isReverse = direction == Direction.RowReverse || direction == Direction.ColumnReverse

		val totalSpacing = spacing * (children.size - 1)
		val availableSpace = if (isVertical) bounds.height - totalSpacing else bounds.width - totalSpacing

		val positions = when (flex) {
			is Flex.Start -> calculateStartPositions(availableSpace, isVertical, isReverse)
			is Flex.End -> calculateEndPositions(availableSpace, isVertical, isReverse)
			is Flex.Center -> calculateCenterPositions(availableSpace, isVertical, isReverse)
			is Flex.SpaceBetween -> calculateSpaceBetweenPositions(availableSpace, isVertical, isReverse)
			is Flex.SpaceAround -> calculateSpaceAroundPositions(availableSpace, isVertical, isReverse)
			is Flex.SpaceEvenly -> calculateSpaceEvenlyPositions(availableSpace, isVertical, isReverse)
			else -> calculateStartPositions(availableSpace, isVertical, isReverse)
		}

		children.forEachIndexed { index, child ->
			if (isVertical) {
				child.properties.position = Position(
					bounds.x + calculateCrossAxisPosition(child, align, bounds.width, false),
					bounds.y + positions[index]
				)
			} else {
				child.properties.position = Position(
					bounds.x + positions[index],
					bounds.y + calculateCrossAxisPosition(child, align, bounds.height, true)
				)
			}
		}
	}

	private fun calculateStartPositions(availableSpace: Float, isVertical: Boolean, isReverse: Boolean): List<Float> {
		val positions = mutableListOf<Float>()
		var currentPos = 0f

		val childrenToProcess = if (isReverse) children.reversed() else children

		childrenToProcess.forEach { child ->
			positions.add(currentPos)
			val childSize = if (isVertical) child.bounds.height else child.bounds.width
			currentPos += childSize + spacing
		}

		return if (isReverse) positions.reversed() else positions
	}

	private fun calculateEndPositions(availableSpace: Float, isVertical: Boolean, isReverse: Boolean): List<Float> {
		val totalContentSize = children.sumOf {
			if (isVertical) it.bounds.height.toDouble() else it.bounds.width.toDouble()
		}.toFloat()
		val offset = availableSpace - totalContentSize

		return calculateStartPositions(availableSpace, isVertical, isReverse).map { it + offset }
	}

	private fun calculateCenterPositions(availableSpace: Float, isVertical: Boolean, isReverse: Boolean): List<Float> {
		val totalContentSize = children.sumOf {
			if (isVertical) it.bounds.height.toDouble() else it.bounds.width.toDouble()
		}.toFloat()
		val offset = (availableSpace - totalContentSize) / 2f

		return calculateStartPositions(availableSpace, isVertical, isReverse).map { it + offset }
	}

	private fun calculateSpaceBetweenPositions(availableSpace: Float, isVertical: Boolean, isReverse: Boolean): List<Float> {
		if (children.size <= 1) return calculateStartPositions(availableSpace, isVertical, isReverse)

		val totalContentSize = children.sumOf {
			if (isVertical) it.bounds.height.toDouble() else it.bounds.width.toDouble()
		}.toFloat()
		val extraSpace = (availableSpace - totalContentSize) / (children.size - 1)

		val positions = mutableListOf<Float>()
		var currentPos = 0f

		children.forEach { child ->
			positions.add(currentPos)
			val childSize = if (isVertical) child.bounds.height else child.bounds.width
			currentPos += childSize + extraSpace
		}

		return if (isReverse) positions.reversed() else positions
	}

	private fun calculateSpaceAroundPositions(availableSpace: Float, isVertical: Boolean, isReverse: Boolean): List<Float> {
		val totalContentSize = children.sumOf {
			if (isVertical) it.bounds.height.toDouble() else it.bounds.width.toDouble()
		}.toFloat()
		val extraSpace = (availableSpace - totalContentSize) / children.size
		val halfSpace = extraSpace / 2f

		val positions = mutableListOf<Float>()
		var currentPos = halfSpace

		children.forEach { child ->
			positions.add(currentPos)
			val childSize = if (isVertical) child.bounds.height else child.bounds.width
			currentPos += childSize + extraSpace
		}

		return if (isReverse) positions.reversed() else positions
	}

	private fun calculateSpaceEvenlyPositions(availableSpace: Float, isVertical: Boolean, isReverse: Boolean): List<Float> {
		val totalContentSize = children.sumOf {
			if (isVertical) it.bounds.height.toDouble() else it.bounds.width.toDouble()
		}.toFloat()
		val extraSpace = (availableSpace - totalContentSize) / (children.size + 1)

		val positions = mutableListOf<Float>()
		var currentPos = extraSpace

		children.forEach { child ->
			positions.add(currentPos)
			val childSize = if (isVertical) child.bounds.height else child.bounds.width
			currentPos += childSize + extraSpace
		}

		return if (isReverse) positions.reversed() else positions
	}

	private fun calculateCrossAxisPosition(child: UIElement, align: Align, containerSize: Float, isVertical: Boolean): Float {
		val childSize = if (isVertical) child.bounds.height else child.bounds.width

		return when (align) {
			is Align.Start -> 0f
			is Align.End -> containerSize - childSize
			is Align.Center -> (containerSize - childSize) / 2f
			is Align.Stretch -> {
				if (isVertical) {
					child.properties.size = child.properties.size.copy(height = containerSize)
				} else {
					child.properties.size = child.properties.size.copy(width = containerSize)
				}
				0f
			}
			else -> 0f
		}
	}

	override fun draw() {
		if (Props.DEBUG) {
			NVcanvas.strokeRect(properties.position.x, properties.position.y, properties.size.width, properties.size.height, Colors.Debug.Blue, 1f)
		}
	}
}

fun UIElement.stack(attrib: Attrib = Attrib, spacing: Float = 0f, direction: Direction = Direction.Row, flex: Flex = Flex.Start, align: Align = Align.Start, init: UIStack.() -> Unit): UIStack {
	val stack = UIStack(attrib, spacing, direction, flex, align)
	addChild(stack)
	stack.init()
	return stack
}