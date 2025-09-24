package cc.lapiz.solstice.core.ui

import cc.lapiz.solstice.core.ui.animate.*

@JvmInline value class Px(val value: () -> Float) {
	constructor(value: Float) : this({ value })

	operator fun invoke() = value()
	operator fun plus(other: Px): Px = Px(this() + other.value())
	operator fun minus(other: Px): Px = Px(this() - other.value())
	operator fun times(other: Float): Px = Px(this() * other)
	operator fun div(other: Float): Px = Px(this() / other)
	override fun toString() = "${this()}px"
}

val Float.px get() = Px { this }
val Int.px get() = Px { this.toFloat() }
val Double.px get() = Px { this.toFloat() }
val Number.px get() = Px { this.toFloat() }

val <T : Number> AnimatedProperty<T>.px get() = Px { this.value.toFloat() }
