package cc.lapiz.solstice.core.data

import cc.lapiz.solstice.core.utils.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.joml.Matrix4f
import java.lang.Math
import java.nio.*

@Serializable
class Transform {
	var position = Vector2()
	var scale = Vector2(1f, 1f)
	var rotation = 0f
	var z = 0f

	@Transient
	var parent: Transform? = null
	@Transient
	private val children = mutableListOf<Transform>()

	@Transient
	private val buffer = Buffers.createFloatBuffer(16)

	fun addChild(child: Transform) {
		children.add(child)
		child.parent = this
	}

	fun removeChild(child: Transform) {
		children.remove(child)
		child.parent = null
	}

	fun matrix(): Matrix4f {
		val local = Matrix4f()
			.translate(position.x, position.y, z)
			.rotateZ(Math.toRadians(rotation.toDouble()).toFloat())
			.scale(scale.x, scale.y, 1f)

		return if (parent != null) {
			Matrix4f(parent!!.matrix()).mul(local)
		} else {
			local
		}
	}

	fun store(): FloatBuffer = matrix().get(buffer)
}
