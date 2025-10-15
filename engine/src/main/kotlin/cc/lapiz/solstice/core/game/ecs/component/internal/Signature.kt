package cc.lapiz.solstice.game.ecs.component.internal

import kotlin.reflect.KClass

class Signature(private val bits: Long = 0L) {
	fun with(type: KClass<*>): Signature =
		Signature(bits or (1L shl ComponentRegistry.id(type)))
	fun containsAll(other: Signature) = (bits and other.bits) == other.bits
	override fun toString() = bits.toString(2)
}