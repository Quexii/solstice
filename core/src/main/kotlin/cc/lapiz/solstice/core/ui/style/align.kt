package cc.lapiz.solstice.core.ui.style

import cc.lapiz.solstice.core.ui.widget.Widget

interface Alignment {
	fun align(children: List<Widget>, availableWidth: Float, availableHeight: Float)

	companion object {
		val TopStart = object : Alignment {
			override fun align(children: List<Widget>, availableWidth: Float, availableHeight: Float) {}
		}

		val TopCenter = object : Alignment {
			override fun align(children: List<Widget>, availableWidth: Float, availableHeight: Float) {
				if (children.isEmpty()) return
				val minX = children.minOf { it.x }
				val maxX = children.maxOf { it.x + it.width + it.padding.left + it.padding.right }
				val totalWidth = maxX - minX
				val offsetX = (availableWidth - totalWidth) / 2 - minX
				children.forEach { child -> child.x += offsetX }
			}
		}

		val TopEnd = object : Alignment {
			override fun align(children: List<Widget>, availableWidth: Float, availableHeight: Float) {
				if (children.isEmpty()) return
				val minX = children.minOf { it.x }
				val maxX = children.maxOf { it.x + it.width + it.padding.left + it.padding.right }
				val totalWidth = maxX - minX
				val offsetX = availableWidth - totalWidth - minX
				children.forEach { child -> child.x += offsetX }
			}
		}

		val CenterStart = object : Alignment {
			override fun align(children: List<Widget>, availableWidth: Float, availableHeight: Float) {
				if (children.isEmpty()) return
				val minY = children.minOf { it.y }
				val maxY = children.maxOf { it.y + it.height + it.padding.top + it.padding.bottom }
				val totalHeight = maxY - minY
				val offsetY = (availableHeight - totalHeight) / 2 - minY
				children.forEach { child -> child.y += offsetY }
			}
		}

		val Center = object : Alignment {
			override fun align(children: List<Widget>, availableWidth: Float, availableHeight: Float) {
				if (children.isEmpty()) return
				val minX = children.minOf { it.x }
				val maxX = children.maxOf { it.x + it.width + it.padding.left + it.padding.right }
				val minY = children.minOf { it.y }
				val maxY = children.maxOf { it.y + it.height + it.padding.top + it.padding.bottom }
				val totalWidth = maxX - minX
				val totalHeight = maxY - minY
				val offsetX = (availableWidth - totalWidth) / 2 - minX
				val offsetY = (availableHeight - totalHeight) / 2 - minY
				children.forEach { child ->
					child.x += offsetX
					child.y += offsetY
				}
			}
		}

		val CenterEnd = object : Alignment {
			override fun align(children: List<Widget>, availableWidth: Float, availableHeight: Float) {
				if (children.isEmpty()) return
				val minX = children.minOf { it.x }
				val maxX = children.maxOf { it.x + it.width + it.padding.left + it.padding.right }
				val minY = children.minOf { it.y }
				val maxY = children.maxOf { it.y + it.height + it.padding.top + it.padding.bottom }
				val totalWidth = maxX - minX
				val totalHeight = maxY - minY
				val offsetX = availableWidth - totalWidth - minX
				val offsetY = (availableHeight - totalHeight) / 2 - minY
				children.forEach { child ->
					child.x += offsetX
					child.y += offsetY
				}
			}
		}

		val BottomStart = object : Alignment {
			override fun align(children: List<Widget>, availableWidth: Float, availableHeight: Float) {
				if (children.isEmpty()) return
				val minY = children.minOf { it.y }
				val maxY = children.maxOf { it.y + it.height + it.padding.top + it.padding.bottom }
				val totalHeight = maxY - minY
				val offsetY = availableHeight - totalHeight - minY
				children.forEach { child -> child.y += offsetY }
			}
		}

		val BottomCenter = object : Alignment {
			override fun align(children: List<Widget>, availableWidth: Float, availableHeight: Float) {
				if (children.isEmpty()) return
				val minX = children.minOf { it.x }
				val maxX = children.maxOf { it.x + it.width + it.padding.left + it.padding.right }
				val minY = children.minOf { it.y }
				val maxY = children.maxOf { it.y + it.height + it.padding.top + it.padding.bottom }
				val totalWidth = maxX - minX
				val totalHeight = maxY - minY
				val offsetX = (availableWidth - totalWidth) / 2 - minX
				val offsetY = availableHeight - totalHeight - minY
				children.forEach { child ->
					child.x += offsetX
					child.y += offsetY
				}
			}
		}

		val BottomEnd = object : Alignment {
			override fun align(children: List<Widget>, availableWidth: Float, availableHeight: Float) {
				if (children.isEmpty()) return
				val minX = children.minOf { it.x }
				val maxX = children.maxOf { it.x + it.width + it.padding.left + it.padding.right }
				val minY = children.minOf { it.y }
				val maxY = children.maxOf { it.y + it.height + it.padding.top + it.padding.bottom }
				val totalWidth = maxX - minX
				val totalHeight = maxY - minY
				val offsetX = availableWidth - totalWidth - minX
				val offsetY = availableHeight - totalHeight - minY
				children.forEach { child ->
					child.x += offsetX
					child.y += offsetY
				}
			}
		}
	}
}

interface Arrangement {
	fun arrange(children: List<Widget>, availableWidth: Float, availableHeight: Float)

	companion object {
		val Horizontal = object : Arrangement {
			override fun arrange(children: List<Widget>, availableWidth: Float, availableHeight: Float) {
				var currentX = 0f
				children.forEach { child ->
					child.x = currentX
					child.y = 0f
					currentX += child.width + child.padding.left + child.padding.right
				}
			}
		}

		val Vertical = object : Arrangement {
			override fun arrange(children: List<Widget>, availableWidth: Float, availableHeight: Float) {
				var currentY = 0f
				children.forEach { child ->
					child.x = 0f
					child.y = currentY
					currentY += child.height + child.padding.top + child.padding.bottom
				}
			}
		}

		val SpaceBetween = object : Arrangement {
			override fun arrange(children: List<Widget>, availableWidth: Float, availableHeight: Float) {
				if (children.isEmpty()) return
				if (children.size == 1) {
					children[0].x = 0f
					children[0].y = 0f
					return
				}
				val totalChildWidth = children.sumOf {
					(it.width + it.padding.left + it.padding.right).toDouble()
				}.toFloat()
				val spacing = (availableWidth - totalChildWidth) / (children.size - 1)
				var currentX = 0f
				children.forEach { child ->
					child.x = currentX
					child.y = 0f
					currentX += child.width + child.padding.left + child.padding.right + spacing
				}
			}
		}

		val SpaceAround = object : Arrangement {
			override fun arrange(children: List<Widget>, availableWidth: Float, availableHeight: Float) {
				if (children.isEmpty()) return
				val totalChildWidth = children.sumOf {
					(it.width + it.padding.left + it.padding.right).toDouble()
				}.toFloat()
				val spacing = (availableWidth - totalChildWidth) / children.size
				var currentX = spacing / 2
				children.forEach { child ->
					child.x = currentX
					child.y = 0f
					currentX += child.width + child.padding.left + child.padding.right + spacing
				}
			}
		}

		val SpaceEvenly = object : Arrangement {
			override fun arrange(children: List<Widget>, availableWidth: Float, availableHeight: Float) {
				if (children.isEmpty()) return
				val totalChildWidth = children.sumOf {
					(it.width + it.padding.left + it.padding.right).toDouble()
				}.toFloat()
				val spacing = (availableWidth - totalChildWidth) / (children.size + 1)
				var currentX = spacing
				children.forEach { child ->
					child.x = currentX
					child.y = 0f
					currentX += child.width + child.padding.left + child.padding.right + spacing
				}
			}
		}

		val VerticalSpaceBetween = object : Arrangement {
			override fun arrange(children: List<Widget>, availableWidth: Float, availableHeight: Float) {
				if (children.isEmpty()) return
				if (children.size == 1) {
					children[0].x = 0f
					children[0].y = 0f
					return
				}
				val totalChildHeight = children.sumOf {
					(it.height + it.padding.top + it.padding.bottom).toDouble()
				}.toFloat()
				val spacing = (availableHeight - totalChildHeight) / (children.size - 1)
				var currentY = 0f
				children.forEach { child ->
					child.x = 0f
					child.y = currentY
					currentY += child.height + child.padding.top + child.padding.bottom + spacing
				}
			}
		}

		val VerticalSpaceAround = object : Arrangement {
			override fun arrange(children: List<Widget>, availableWidth: Float, availableHeight: Float) {
				if (children.isEmpty()) return
				val totalChildHeight = children.sumOf {
					(it.height + it.padding.top + it.padding.bottom).toDouble()
				}.toFloat()
				val spacing = (availableHeight - totalChildHeight) / children.size
				var currentY = spacing / 2
				children.forEach { child ->
					child.x = 0f
					child.y = currentY
					currentY += child.height + child.padding.top + child.padding.bottom + spacing
				}
			}
		}

		val VerticalSpaceEvenly = object : Arrangement {
			override fun arrange(children: List<Widget>, availableWidth: Float, availableHeight: Float) {
				if (children.isEmpty()) return
				val totalChildHeight = children.sumOf {
					(it.height + it.padding.top + it.padding.bottom).toDouble()
				}.toFloat()
				val spacing = (availableHeight - totalChildHeight) / (children.size + 1)
				var currentY = spacing
				children.forEach { child ->
					child.x = 0f
					child.y = currentY
					currentY += child.height + child.padding.top + child.padding.bottom + spacing
				}
			}
		}
	}
}
