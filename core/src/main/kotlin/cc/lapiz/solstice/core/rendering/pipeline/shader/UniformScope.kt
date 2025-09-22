package cc.lapiz.solstice.core.rendering.pipeline.shader

import cc.lapiz.solstice.core.rendering.platform.Graphics
import java.nio.FloatBuffer

class UniformScope(private val uniformMap: Map<String, Uniform>) {
	private fun getLocation(name: String): Int = uniformMap[name]?.location ?: error("Uniform '$name' not found in shader!")

	fun hasUniform(name: String): Boolean = uniformMap.containsKey(name)

	fun float(name: String, value: Float) {
		require(hasUniform(name)) { "Uniform '$name' not found!" }
		require(uniformMap[name]!!.type == "float") { "Uniform '$name' is not of type float!" }
		Graphics.uniform1f(getLocation(name), value)
	}

	fun vec2(name: String, x: Float, y: Float) {
		require(hasUniform(name)) { "Uniform '$name' not found!" }
		require(uniformMap[name]!!.type == "vec2") { "Uniform '$name' is not of type vec2!" }
		Graphics.uniform2f(getLocation(name), x, y)
	}

	fun vec3(name: String, x: Float, y: Float, z: Float) {
		require(hasUniform(name)) { "Uniform '$name' not found!" }
		require(uniformMap[name]!!.type == "vec3") { "Uniform '$name' is not of type vec3!" }
		Graphics.uniform3f(getLocation(name), x, y, z)
	}

	fun vec4(name: String, x: Float, y: Float, z: Float, w: Float) {
		require(hasUniform(name)) { "Uniform '$name' not found!" }
		require(uniformMap[name]!!.type == "vec4") { "Uniform '$name' is not of type vec4!" }
		Graphics.uniform4f(getLocation(name), x, y, z, w)
	}

	fun mat4(name: String, matrix: FloatBuffer) {
		require(hasUniform(name)) { "Uniform '$name' not found!" }
		require(uniformMap[name]!!.type == "mat4") { "Uniform '$name' is not of type mat4!" }
		Graphics.uniformMatrix4fv(getLocation(name), false, matrix)
	}

	fun int(name: String, value: Int) {
		require(hasUniform(name)) { "Uniform '$name' not found!" }
		require(uniformMap[name]!!.type == "int") { "Uniform '$name' is not of type int!" }
		Graphics.uniform1i(getLocation(name), value)
	}

	fun sampler2D(name: String, unit: Int) {
		require(hasUniform(name)) { "Uniform '$name' not found!" }
		require(uniformMap[name]!!.type == "sampler2D") { "Uniform '$name' is not of type sampler2D!" }
		Graphics.uniform1i(getLocation(name), unit)
	}
}