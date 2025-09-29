package cc.lapiz.solstice.core.rendering.pipeline.mesh

import cc.lapiz.solstice.core.rendering.pipeline.vertex.*
import cc.lapiz.solstice.core.utils.*
import java.nio.*

class MeshBuilder(private val layout: VertexLayout, initialCapacity: Int = 1024) {
	private var mode: Mesh.Mode? = null
	private var usage: Mesh.Usage = Mesh.Usage.DYNAMIC
	private var building = false

	private val buffer: FloatBuffer = Buffers.createFloatBuffer(initialCapacity)
	private val tempBuffer: FloatArray
	private var tempIndex = 0
	private var attributeIndex = 0
	private var vertexCount = 0
	private val attributes = layout.getAttributes().map { it.type }

	init {
		tempBuffer = FloatArray(attributes.sumOf { it.componentCount })
	}

	fun begin(mode: Mesh.Mode, usage: Mesh.Usage = Mesh.Usage.DYNAMIC): MeshBuilder {
		if (building) throw IllegalStateException("Already building a mesh!")
		building = true
		this.mode = mode
		this.usage = usage
		buffer.clear()
		tempIndex = 0
		attributeIndex = 0
		vertexCount = 0
		return this
	}

	private fun check(type: VertexAttributeType) {
		if (!building) throw IllegalStateException("Not building!")
		if (attributes[attributeIndex++] != type) {
			throw IllegalStateException("Expected attribute $type")
		}
	}

	fun pos(x: Float, y: Float): MeshBuilder {
		check(VertexAttributeType.VEC2)
		tempBuffer[tempIndex++] = x
		tempBuffer[tempIndex++] = y
		return this
	}

	fun tex(u: Float, v: Float): MeshBuilder {
		check(VertexAttributeType.VEC2)
		tempBuffer[tempIndex++] = u
		tempBuffer[tempIndex++] = v
		return this
	}

	fun color(r: Float, g: Float, b: Float, a: Float): MeshBuilder {
		check(VertexAttributeType.VEC4)
		tempBuffer[tempIndex++] = r
		tempBuffer[tempIndex++] = g
		tempBuffer[tempIndex++] = b
		tempBuffer[tempIndex++] = a
		return this
	}

	fun endVertex(): MeshBuilder {
		buffer.put(tempBuffer, 0, tempIndex)
		tempIndex = 0
		attributeIndex = 0
		vertexCount++
		return this
	}

	fun build(): Mesh {
		if (!building) throw IllegalStateException("Not building a mesh!")
		building = false
		buffer.flip()
		return Mesh(mode!!, layout, vertexCount, buffer.toArray(), usage)
	}

	private fun FloatBuffer.toArray(): Array<Float> {
		val arr = FloatArray(limit())
		get(arr)
		return arr.toTypedArray()
	}
}
