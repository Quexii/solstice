package cc.lapiz.solstice.core.rendering.pipeline.mesh

import cc.lapiz.solstice.core.rendering.pipeline.vertex.VertexLayout
import kotlin.math.cos
import kotlin.math.sin

object Meshes {
	private var _uvRect: Mesh? = null
	private val _circleCache = mutableMapOf<Int, Mesh>()

	fun init() {
		_uvRect = createUVRect()
		_circleCache[16] = createUVCircle(16)
		_circleCache[32] = createUVCircle(32)
		_circleCache[64] = createUVCircle(64)
	}

	private fun createUVRect(): Mesh {
		val builder = MeshBuilder(VertexLayout.PosTex)
		builder.begin(Mesh.Mode.TRIANGLES)
		builder.pos(-1f, -1f).tex(0f, 0f).endVertex()
		builder.pos(1f, -1f).tex(1f, 0f).endVertex()
		builder.pos(1f, 1f).tex(1f, 1f).endVertex()
		builder.pos(-1f, -1f).tex(0f, 0f).endVertex()
		builder.pos(1f, 1f).tex(1f, 1f).endVertex()
		builder.pos(-1f, 1f).tex(0f, 1f).endVertex()
		return builder.build()
	}

	private fun createUVCircle(segments: Int): Mesh {
		val builder = MeshBuilder(VertexLayout.PosTex)
		builder.begin(Mesh.Mode.TRIANGLE_FAN)

		builder.pos(0f, 0f).tex(0.5f, 0.5f).endVertex()

		val n = segments.coerceAtLeast(3)
		val step = 2.0 * Math.PI / n.toDouble()
		for (i in 0..n) {
			val angle = i * step
			val x = (cos(angle)).toFloat()
			val y = (sin(angle)).toFloat()

			val u = 0.5f + x * 0.5f
			val v = 0.5f + y * 0.5f

			builder.pos(x, y).tex(u, v).endVertex()
		}

		return builder.build()
	}

	val UVRect: Mesh
		get() = _uvRect ?: error("Meshes not initialized")

	val Circle16: Mesh
		get() = _circleCache[16] ?: error("Meshes not initialized")

	val Circle32: Mesh
		get() = _circleCache[32] ?: error("Meshes not initialized")

	val Circle64: Mesh
		get() = _circleCache[64] ?: error("Meshes not initialized")
}