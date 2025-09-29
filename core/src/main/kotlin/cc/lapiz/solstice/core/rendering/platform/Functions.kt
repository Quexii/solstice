package cc.lapiz.solstice.core.rendering.platform

import cc.lapiz.solstice.processor.GenerateGL
import java.nio.Buffer
import java.nio.FloatBuffer

@GenerateGL
interface Functions {
	fun clear(mask: Int)
	fun clearColor(r: Float, g: Float, b: Float, a: Float)
	fun enable(cap: Int)
	fun disable(cap: Int)
	fun viewport(x: Int, y: Int, width: Int, height: Int)
	fun createProgram(): Int
	fun createShader(type: Int): Int
	fun shaderSource(shader: Int, source: String)
	fun compileShader(shader: Int)
	fun attachShader(program: Int, shader: Int)
	fun linkProgram(program: Int)
	fun useProgram(program: Int)
	fun getProgrami(program: Int, pname: Int): Int
	fun getShaderi(shader: Int, pname: Int): Int
	fun getProgramInfoLog(program: Int): String
	fun getShaderInfoLog(shader: Int): String
	fun getUniformLocation(program: Int, name: String): Int
	fun uniform1i(location: Int, v0: Int)
	fun uniform1f(location: Int, v0: Float)
	fun uniform2f(location: Int, v0: Float, v1: Float)
	fun uniform3f(location: Int, v0: Float, v1: Float, v2: Float)
	fun uniform4f(location: Int, v0: Float, v1: Float, v2: Float, v3: Float)
	fun uniformMatrix4fv(location: Int, transpose: Boolean, value: FloatBuffer)
	fun activeTexture(texture: Int)
	fun bindTexture(target: Int, texture: Int)
	fun genTextures(): Int
	fun deleteTextures(texture: Int)
	fun texImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, type: Int, pixels: Buffer?)
	fun texParameteri(target: Int, pname: Int, param: Int)
	fun generateMipmap(target: Int)
	fun deleteProgram(program: Int)
	fun deleteShader(shader: Int)
	fun deleteBuffers(buffer: Int)
	fun deleteVertexArrays(vao: Int)
	fun genBuffers(): Int
	fun genVertexArrays(): Int
	fun bindBuffer(target: Int, buffer: Int)
	fun bindVertexArray(vao: Int)
	fun bufferData(target: Int, data: Long, usage: Int)
	fun bufferData(target: Int, data: Buffer, usage: Int)
	fun bufferData(target: Int, data: Array<Float>, usage: Int)
	fun bufferSubData(target: Int, offset: Long, data: Buffer)
	fun bufferSubData(target: Int, offset: Long, data: Array<Float>)
	fun vertexAttribPointer(index: Int, size: Int, type: Int, normalized: Boolean, stride: Int, offset: Long)
	fun vertexAttribIPointer(index: Int, size: Int, type: Int, stride: Int, offset: Long)
	fun vertexAttribDivisor(index: Int, divisor: Int)
	fun enableVertexAttribArray(index: Int)
	fun drawElements(mode: Int, count: Int, type: Int, indices: Long)
	fun drawElementsInstanced(mode: Int, count: Int, type: Int, indices: Long, instanceCount: Int)
	fun drawArrays(mode: Int, first: Int, count: Int)
	fun drawArraysInstanced(mode: Int, first: Int, count: Int, instanceCount: Int)
	fun blendFunc(sfactor: Int, dfactor: Int)
	fun blendFuncSeparate(sfactorRGB: Int, dfactorRGB: Int, sfactorAlpha: Int, dfactorAlpha: Int)
	fun stencilFunc(func: Int, ref: Int, mask: Int)
	fun stencilOp(fail: Int, zfail: Int, zpass: Int)
	fun stencilMask(mask: Int)
	fun depthFunc(func: Int)
	fun depthMask(flag: Boolean)
	fun getError(): Int
	fun polygonMode(face: Int, mode: Int)
}