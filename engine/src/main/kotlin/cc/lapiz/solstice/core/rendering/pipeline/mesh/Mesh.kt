package cc.lapiz.solstice.rendering.pipeline.mesh

import cc.lapiz.solstice.rendering.pipeline.vertex.*
import org.lwjgl.opengl.*

class Mesh(private var mode: Mode, val layout: VertexLayout, val vertexCount: Int, val vertexData: Array<Float>, private var usage: Usage) {
	enum class Mode(val value: Int) {
		TRIANGLES(GL33C.GL_TRIANGLES),
		TRIANGLE_FAN(GL33C.GL_TRIANGLE_FAN),
		LINES(GL33C.GL_LINES),
		LINE_STRIP(GL33C.GL_LINE_STRIP),
		LINE_LOOP(GL33C.GL_LINE_LOOP),
		POINTS(GL33C.GL_POINTS);
	}

	enum class Usage(val value: Int) {
		STATIC(GL33C.GL_STATIC_DRAW),
		DYNAMIC(GL33C.GL_DYNAMIC_DRAW),
		STREAM(GL33C.GL_STREAM_DRAW);
	}

	private var vao = 0
	private var vbo = 0
	private var uploaded = false

	fun init() {
		vao = GL33C.glGenVertexArrays()
		vbo = GL33C.glGenBuffers()

		GL33C.glBindVertexArray(vao)
		GL33C.glBindBuffer(GL33C.GL_ARRAY_BUFFER, vbo)
		GL33C.glBufferData(GL33C.GL_ARRAY_BUFFER, vertexData.toFloatArray(), usage.value)

		var offset = 0L
		layout.getAttributes().forEachIndexed { index, attr ->
			GL33C.glVertexAttribPointer(index, attr.type.componentCount, attr.type.get(), false, layout.getStride(), offset * Float.SIZE_BYTES)
			GL33C.glEnableVertexAttribArray(index)
			offset += attr.type.componentCount
		}
		GL33C.glBindBuffer(GL33C.GL_ARRAY_BUFFER, 0)
		GL33C.glBindVertexArray(0)
		uploaded = true
	}

	fun render() {
		if (!uploaded) init()
		GL33C.glBindVertexArray(vao)
		GL33C.glDrawArrays(mode.value, 0, vertexCount)
		GL33C.glBindVertexArray(0)
	}

	fun setMode(mode: Mode) {
		this.mode = mode
	}
}