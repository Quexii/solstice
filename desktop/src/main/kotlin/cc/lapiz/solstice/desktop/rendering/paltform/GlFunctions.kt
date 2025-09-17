package cc.lapiz.solstice.desktop.rendering.paltform

import cc.lapiz.solstice.core.rendering.platform.Functions
import org.jetbrains.annotations.*
import org.lwjgl.opengl.*
import java.nio.*

class GlFunctions : Functions {
	override fun clear(mask: Int) {
		GL33C.glClear(mask)
	}

	override fun clearColor(r: Float, g: Float, b: Float, a: Float) {
		GL33C.glClearColor(r, g, b, a)
	}

	override fun enable(cap: Int) {
		GL33C.glEnable(cap)
	}

	override fun disable(cap: Int) {
		GL33C.glDisable(cap)
	}

	override fun viewport(x: Int, y: Int, width: Int, height: Int) {
		GL33C.glViewport(x, y, width, height)
	}

	override fun createProgram(): Int = GL33C.glCreateProgram()

	override fun createShader(type: Int): Int = GL33C.glCreateShader(type)

	override fun shaderSource(shader: Int, source: String) {
		GL33C.glShaderSource(shader, source)
	}

	override fun compileShader(shader: Int) {
		GL33C.glCompileShader(shader)
	}

	override fun attachShader(program: Int, shader: Int) {
		GL33C.glAttachShader(program, shader)
	}

	override fun linkProgram(program: Int) {
		GL33C.glLinkProgram(program)
	}

	override fun useProgram(program: Int) {
		GL33C.glUseProgram(program)
	}

	override fun getProgrami(program: Int, pname: Int): Int = GL33C.glGetProgrami(program, pname)

	override fun getShaderi(shader: Int, pname: Int): Int = GL33C.glGetShaderi(shader, pname)

	override fun getProgramInfoLog(program: Int): String = GL33C.glGetProgramInfoLog(program)

	override fun getShaderInfoLog(shader: Int): String = GL33C.glGetShaderInfoLog(shader)

	override fun getUniformLocation(program: Int, name: String): Int = GL33C.glGetUniformLocation(program, name)

	override fun uniform1i(location: Int, v0: Int) {
		GL33C.glUniform1i(location, v0)
	}

	override fun uniform1f(location: Int, v0: Float) {
		GL33C.glUniform1f(location, v0)
	}

	override fun uniform2f(location: Int, v0: Float, v1: Float) {
		GL33C.glUniform2f(location, v0, v1)
	}

	override fun uniform3f(location: Int, v0: Float, v1: Float, v2: Float) {
		GL33C.glUniform3f(location, v0, v1, v2)
	}

	override fun uniform4f(location: Int, v0: Float, v1: Float, v2: Float, v3: Float) {
		GL33C.glUniform4f(location, v0, v1, v2, v3)
	}

	override fun uniformMatrix4fv(location: Int, transpose: Boolean, value: FloatBuffer) {
		GL33C.glUniformMatrix4fv(location, transpose, value)
	}

	override fun activeTexture(texture: Int) {
		GL33C.glActiveTexture(texture)
	}

	override fun bindTexture(target: Int, texture: Int) {
		GL33C.glBindTexture(target, texture)
	}

	override fun genTextures(): Int  = GL33C.glGenTextures()

	override fun deleteTextures(texture: Int) {
		GL33C.glDeleteTextures(texture)
	}

	override fun texImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, type: Int, pixels: Buffer?) {
		when (pixels) {
			is ByteBuffer -> GL33C.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels)
			is FloatBuffer -> GL33C.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels)
			is IntBuffer -> GL33C.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels)
			null -> GL33C.glTexImage2D(target, level, internalformat, width, height, border, format, type, 0)
			else -> {
				throw IllegalArgumentException("Unsupported buffer type: ${pixels.javaClass}")
			}
		}
	}

	override fun texParameteri(target: Int, pname: Int, param: Int) {
		GL33C.glTexParameteri(target, pname, param)
	}

	override fun generateMipmap(target: Int) {
		GL33C.glGenerateMipmap(target)
	}

	override fun deleteProgram(program: Int) {
		GL33C.glDeleteProgram(program)
	}

	override fun deleteShader(shader: Int) {
		GL33C.glDeleteShader(shader)
	}

	override fun deleteBuffers(buffer: Int) {
		GL33C.glDeleteBuffers(buffer)
	}

	override fun deleteVertexArrays(vao: Int) {
		GL33C.glDeleteVertexArrays(vao)
	}

	override fun genBuffers(): Int = GL33C.glGenBuffers()

	override fun genVertexArrays(): Int = GL33C.glGenVertexArrays()

	override fun bindBuffer(target: Int, buffer: Int) {
		GL33C.glBindBuffer(target, buffer)
	}

	override fun bindVertexArray(vao: Int) {
		GL33C.glBindVertexArray(vao)
	}

	override fun bufferData(target: Int, data: Long, usage: Int) {
		GL33C.glBufferData(target, data, usage)
	}

	override fun bufferData(target: Int, data: Buffer, usage: Int) {
		when (data) {
			is FloatBuffer -> GL33C.glBufferData(target, data, usage)
			is IntBuffer -> GL33C.glBufferData(target, data, usage)
			is ByteBuffer -> GL33C.glBufferData(target, data, usage)
			else -> {
				throw IllegalArgumentException("Unsupported buffer type: ${data.javaClass}")
			}
		}
	}

	override fun bufferSubData(target: Int, offset: Long, data: Buffer) {
		when (data) {
			is FloatBuffer -> GL33C.glBufferSubData(target, offset, data)
			is IntBuffer -> GL33C.glBufferSubData(target, offset, data)
			is ByteBuffer -> GL33C.glBufferSubData(target, offset, data)
			else -> {
				throw IllegalArgumentException("Unsupported buffer type: ${data.javaClass}")
			}
		}
	}

	override fun vertexAttribPointer(index: Int, size: Int, type: Int, normalized: Boolean, stride: Int, offset: Long) {
		GL33C.glVertexAttribPointer(index, size, type, normalized, stride, offset)
	}

	override fun vertexAttribIPointer(index: Int, size: Int, type: Int, stride: Int, offset: Long) {
		GL33C.glVertexAttribIPointer(index, size, type, stride, offset)
	}

	override fun vertexAttribDivisor(index: Int, divisor: Int) {
		GL33C.glVertexAttribDivisor(index, divisor)
	}

	override fun enableVertexAttribArray(index: Int) {
		GL33C.glEnableVertexAttribArray(index)
	}

	override fun drawElements(mode: Int, count: Int, type: Int, indices: Long) {
		GL33C.glDrawElements(mode, count, type, indices)
	}

	override fun drawElementsInstanced(mode: Int, count: Int, type: Int, indices: Long, instanceCount: Int) {
		GL33C.glDrawElementsInstanced(mode, count, type, indices, instanceCount)
	}

	override fun drawArrays(mode: Int, first: Int, count: Int) {
		GL33C.glDrawArrays(mode, first, count)
	}

	override fun drawArraysInstanced(mode: Int, first: Int, count: Int, instanceCount: Int) {
		GL33C.glDrawArraysInstanced(mode, first, count, instanceCount)
	}
}