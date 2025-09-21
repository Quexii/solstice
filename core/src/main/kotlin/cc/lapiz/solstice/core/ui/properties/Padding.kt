package cc.lapiz.solstice.core.ui.properties

data class Padding(var left: Float, var top: Float, var right: Float, var bottom: Float) {
	constructor(padding: Float) : this(padding, padding, padding, padding)
	constructor(horizontal: Float, vertical: Float) : this(horizontal, vertical, horizontal, vertical)
	constructor() : this(0f, 0f, 0f, 0f)
}
