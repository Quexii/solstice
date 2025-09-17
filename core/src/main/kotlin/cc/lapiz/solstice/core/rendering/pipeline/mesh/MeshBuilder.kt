package cc.lapiz.solstice.core.rendering.pipeline.mesh

import cc.lapiz.solstice.core.rendering.*
import cc.lapiz.solstice.core.rendering.pipeline.vertex.*
import cc.lapiz.solstice.core.utils.*

private typealias Type = VertexAttributeType

class MeshBuilder {
	private var currentLayout: VertexLayout? = null
	private lateinit var mode: Mesh.Mode
	private lateinit var usage: Mesh.Usage
	private val vertices = Buffers.createFloatBuffer(512)
	private lateinit var tempBuffer: FloatArray
	private var tempBufferIndex = 0
	private var building = false
	private var attributes: List<Type>? = null
	private var attributeIndex = 0
	private var vertexCount = 0

	fun begin(mode: Mesh.Mode, usage: Mesh.Usage = Mesh.Usage.DYNAMIC) {
		if (building) throw IllegalStateException("Already building a mesh!")
		building = true
		this.mode = mode
		this.usage = usage
		currentLayout = RenderSystem.currentShader()?.layout
		vertices.clear()
		attributes = currentLayout?.getAttributes()?.map { it.type }
		val size = currentLayout?.getAttributes()?.sumOf { it.type.componentCount } ?: 0
		tempBuffer = FloatArray(size)
		tempBufferIndex = 0
	}

	fun pos(x: Float, y: Float): MeshBuilder {
		if (check(Type.VEC2)) {
			put(x)
			put(y)
		}
		return this
	}

	fun color(r: Float, g: Float, b: Float, a: Float): MeshBuilder {
		if (check(Type.VEC4)) {
			put(r)
			put(g)
			put(b)
			put(a)
		}
		return this
	}

	fun tex(u: Float, v: Float): MeshBuilder {
		if (check(Type.VEC2)) {
			put(u)
			put(v)
		}
		return this
	}

	fun endVertex(): MeshBuilder {
		if (!building) throw IllegalStateException("Not building a mesh!")
		if (currentLayout == null) throw IllegalStateException("No current vertex layout!")
		if (attributes == null) throw IllegalStateException("No current attributes!")
		if (attributeIndex != attributes!!.size) throw IllegalStateException("Not all attributes set! (${attributeIndex}/${attributes!!.size})")
		vertices.put(tempBuffer)
		tempBufferIndex = 0
		attributeIndex = 0
		vertexCount++
		return this
	}

	private fun check(type: Type): Boolean {
		if (!building) throw IllegalStateException("Not building a mesh!")
		if (currentLayout == null) throw IllegalStateException("No current vertex layout!")
		if (attributes == null) throw IllegalStateException("No current attributes!")
		if (attributes!![attributeIndex++] != type) throw IllegalStateException("Expected attribute $type")
		return true
	}

	private fun put(value: Float) {
		tempBuffer[tempBufferIndex++] = value
	}

	fun build(): Mesh {
		if (!building) throw IllegalStateException("Not building a mesh!")
		if (currentLayout == null) throw IllegalStateException("No current vertex layout!")
		if (attributes == null) throw IllegalStateException("No current attributes!")
		if (attributeIndex != 0) throw IllegalStateException("Unfinished vertex! (${attributeIndex}/${attributes!!.size})")
		building = false
		vertices.flip()
		val mesh = Mesh(mode, currentLayout!!, vertexCount, vertices, usage)
		cleanup()
		return mesh
	}

	fun cleanup() {
		if (building) throw IllegalStateException("Cannot clean up while building a mesh!")
		vertices.clear()
		vertexCount = 0
		attributeIndex = 0
		tempBufferIndex = 0
		attributes = null
		currentLayout = null
	}
}