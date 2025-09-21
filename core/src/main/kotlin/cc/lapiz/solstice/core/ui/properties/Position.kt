package cc.lapiz.solstice.core.ui.properties

data class Position(var x: Float, var y: Float) {
	constructor(x: Float) : this(x, x)
	constructor() : this(0f, 0f)
}
