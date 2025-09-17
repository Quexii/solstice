package cc.lapiz.solstice.core

import cc.lapiz.solstice.core.event.*
import cc.lapiz.solstice.core.rendering.*
import cc.lapiz.solstice.core.rendering.pipeline.mesh.*
import cc.lapiz.solstice.core.time.*
import cc.lapiz.solstice.core.utils.*
import cc.lapiz.solstice.core.window.*

open class Platform {
	protected val LOGGER = logger(this::class.java)

	lateinit var timer: Timer
		protected set

	private lateinit var mesh: Mesh
	private val mb = MeshBuilder()

	open fun start() {
		RenderSystem.camera().setViewport(Window.width(), Window.height())

		RenderSystem.setShader { FillShader }
		mb.begin(Mesh.Mode.TRIANGLES)
		mb.pos(0f, 0f).color(1f, 0f, 0f, 1f).endVertex()
		mb.pos(100f, 0f).color(0f, 1f, 0f, 1f).endVertex()
		mb.pos(0f, 100f).color(0f, 0f, 1f, 1f).endVertex()
		mesh = mb.build()
		mesh.init()
	}

	protected fun update(delta: Float) {

	}

	protected fun event(event: Event) {
		if (event is WindowEvent) Window.handleEvents(event)
		println("Event: $event")
	}

	protected fun render() {
		RenderSystem.renderMesh(mesh)
	}
}