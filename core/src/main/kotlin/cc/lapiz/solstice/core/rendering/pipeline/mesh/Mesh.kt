package cc.lapiz.solstice.core.rendering.pipeline.mesh

import cc.lapiz.solstice.core.rendering.pipeline.vertex.VertexLayout
import cc.lapiz.solstice.core.rendering.platform.Graphics
import java.nio.FloatBuffer

class Mesh(private var mode: Mode, val layout: VertexLayout, val vertexCount: Int, val vertexData: FloatBuffer, private var usage: Usage) {
	enum class Mode(val value: Int) {
		TRIANGLES(Graphics.TRIANGLES),
		LINES(Graphics.LINES),
		LINE_STRIP(Graphics.LINE_STRIP),
		POINTS(Graphics.POINTS);
	}

	enum class Usage(val value: Int) {
		STATIC(Graphics.STATIC_DRAW),
		DYNAMIC(Graphics.DYNAMIC_DRAW),
		STREAM(Graphics.STREAM_DRAW);
	}

	private var vao = 0
	private var vbo = 0
	private var uploaded = false

	fun init() {
		vao = Graphics.genVertexArrays()
		Graphics.bindVertexArray(vao)
		vbo = Graphics.genBuffers()
		Graphics.bindBuffer(Graphics.ARRAY_BUFFER, vbo)
		Graphics.bufferData(Graphics.ARRAY_BUFFER, vertexData, usage.value)

		var offset = 0L
		layout.getAttributes().forEachIndexed { index, attr ->
			Graphics.enableVertexAttribArray(index)
			Graphics.vertexAttribPointer(index, attr.type.componentCount, attr.type.get(), false, layout.getStride() * 4, offset)
			offset += attr.type.componentCount * 4
		}
		Graphics.bindBuffer(Graphics.ARRAY_BUFFER, 0)
		Graphics.bindVertexArray(0)
		uploaded = true
	}

	fun render() {
		if (!uploaded) init()
		Graphics.bindVertexArray(vao)
		Graphics.drawArrays(mode.value, 0, vertexCount)
		Graphics.bindVertexArray(0)
	}

	fun setMode(mode: Mode) {
		this.mode = mode
	}
}