package cc.lapiz.solstice.data

import kotlinx.serialization.*
import org.joml.Vector2i

@Serializable
class Target(val id: Int, var x: Float, var y: Float, var type: TargetType = TargetType.NONE) {
	@Transient var reached = false
	@Transient var navigating = false
	@Transient var path: MutableList<Vector2i>? = null
}

@Serializable
enum class TargetType {
	ENTITY, POSITION, NONE
}