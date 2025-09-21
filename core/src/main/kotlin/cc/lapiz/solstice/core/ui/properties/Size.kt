package cc.lapiz.solstice.core.ui.properties

private typealias GameWindow = cc.lapiz.solstice.core.window.Window

data class Size(
	var width: Float,
	var height: Float
) {
	constructor(size: Float) : this(size, size)
	constructor() : this(0f, 0f)

	companion object {
		val Window: Size
			get() = Size(GameWindow.width().toFloat(), GameWindow.height().toFloat())
	}
}