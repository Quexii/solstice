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

	private val model = Matrix4f()
	private val buffer = Buffers.createFloatBuffer(16)

	fun matrix() = model.identity()
		.translate(position.x, position.y, 0f)
		.rotateZ(Math.toRadians(rotation.toDouble()).toFloat())
		.scale(scale.x, scale.y, 1f)

	fun store(): FloatBuffer {
		matrix().get(buffer)
		return buffer.flip()
	}
}