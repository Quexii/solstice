package cc.lapiz.solstice.rendering.pipeline.shader

import org.lwjgl.opengl.GL33C

class Framebuffer() {
	private var width = 0
	private var height = 0
	private var depth = 0
	private var fboId = 0
	private var textureId = 0

	fun init(width: Int, height: Int) {
		this.width = width
		this.height = height

		fboId = GL33C.glGenFramebuffers()
		GL33C.glBindFramebuffer(GL33C.GL_FRAMEBUFFER, fboId)
		textureId = GL33C.glGenTextures()
		GL33C.glBindTexture(GL33C.GL_TEXTURE_2D, textureId)
		GL33C.glTexImage2D(GL33C.GL_TEXTURE_2D, 0, GL33C.GL_RGB, width, height, 0, GL33C.GL_RGB, GL33C.GL_UNSIGNED_BYTE, 0)
		GL33C.glTexParameteri(GL33C.GL_TEXTURE_2D, GL33C.GL_TEXTURE_MIN_FILTER, GL33C.GL_LINEAR)
		GL33C.glTexParameteri(GL33C.GL_TEXTURE_2D, GL33C.GL_TEXTURE_MAG_FILTER, GL33C.GL_LINEAR)
		GL33C.glFramebufferTexture2D(GL33C.GL_FRAMEBUFFER, GL33C.GL_COLOR_ATTACHMENT0, GL33C.GL_TEXTURE_2D, textureId, 0)
		depth = GL33C.glGenRenderbuffers()
		GL33C.glBindRenderbuffer(GL33C.GL_RENDERBUFFER, depth)
		GL33C.glRenderbufferStorage(GL33C.GL_RENDERBUFFER, GL33C.GL_DEPTH24_STENCIL8, width, height)
		GL33C.glFramebufferRenderbuffer(GL33C.GL_FRAMEBUFFER, GL33C.GL_DEPTH_STENCIL_ATTACHMENT, GL33C.GL_RENDERBUFFER, depth)
		if (GL33C.glCheckFramebufferStatus(GL33C.GL_FRAMEBUFFER) != GL33C.GL_FRAMEBUFFER_COMPLETE) {
			throw RuntimeException("Framebuffer is not complete!")
		}

		GL33C.glBindFramebuffer(GL33C.GL_FRAMEBUFFER, 0)
	}

	fun bind(viewport: Boolean = true) {
		GL33C.glBindFramebuffer(GL33C.GL_FRAMEBUFFER, fboId)
		if (viewport) GL33C.glViewport(0, 0, width, height)
	}

	fun unbind() {
		GL33C.glBindFramebuffer(GL33C.GL_FRAMEBUFFER, 0)
	}

	fun delete() {
		GL33C.glDeleteFramebuffers(fboId)
		GL33C.glDeleteRenderbuffers(depth)
	}

	fun fboId(): Int = fboId
	fun width(): Int = width
	fun height(): Int = height
	fun textureId(): Int = textureId
}