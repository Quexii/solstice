package cc.lapiz.solstice.core.rendering.pipeline.shader

import cc.lapiz.solstice.core.rendering.platform.Graphics
import java.nio.FloatBuffer

class UniformScope(private val uniformMap: Map<String, Int>) {
	private fun getLocation(name: String): Int = uniformMap[name] ?: error("Uniform '$name' not found in shader!")

	fun hasUniform(name: String): Boolean = uniformMap.containsKey(name)

	fun float(name: String, value: Float) {
		require(hasUniform(name)) { "Uniform '$name' not found!" }
		Graphics.uniform1f(getLocation(name), value)
	}

	fun vec2(name: String, x: Float, y: Float) {
		require(hasUniform(name)) { "Uniform '$name' not found!" }
		Graphics.uniform2f(getLocation(name), x, y)
	}

	fun vec3(name: String, x: Float, y: Float, z: Float) {
		require(hasUniform(name)) { "Uniform '$name' not found!" }
		Graphics.uniform3f(getLocation(name), x, y, z)
	}

	fun vec4(name: String, x: Float, y: Float, z: Float, w: Float) {
		require(hasUniform(name)) { "Uniform '$name' not found!" }
		Graphics.uniform4f(getLocation(name), x, y, z, w)
	}

	fun mat4(name: String, matrix: FloatBuffer) {
		require(hasUniform(name)) { "Uniform '$name' not found!" }
		Graphics.uniformMatrix4fv(getLocation(name), false, matrix)
	}

	fun int(name: String, value: Int) {
		require(hasUniform(name)) { "Uniform '$name' not found!" }
		Graphics.uniform1i(getLocation(name), value)
	}
}