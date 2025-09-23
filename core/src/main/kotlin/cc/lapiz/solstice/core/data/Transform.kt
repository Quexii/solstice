package cc.lapiz.solstice.core.data

import cc.lapiz.solstice.core.utils.Buffers
import org.joml.Matrix4f
import org.joml.Vector2f
import java.lang.Math
import java.nio.FloatBuffer

class Transform {
	var position = Vector2f()
	var scale = Vector2f(1f, 1f)
	var rotation = 0f

	// hierarchy
	var parent: Transform? = null
	private val children = mutableListOf<Transform>()

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
			.translate(position.x, position.y, 0f)
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
