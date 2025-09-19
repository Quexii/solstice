package cc.lapiz.solstice.core.ui.style

data class Padding(
	val left: Float = 0f,
	val right: Float = 0f,
	val top: Float = 0f,
	val bottom: Float = 0f
) {
	constructor(all: Float) : this(all, all, all, all)
}
