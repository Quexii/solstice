package cc.lapiz.solstice.core.rendering.texture

import org.lwjgl.opengl.*
import java.nio.*

object TextureUtil {
	fun createTexture(data: ByteBuffer, w: Int, h: Int, format: Int, type: Int, wrap: Int, mipmap: Boolean, minFilter: Int, magFilter: Int): Int {
		val texure = GL33C.glGenTextures()
		GL33C.glBindTexture(GL33C.GL_TEXTURE_2D, texure)
		GL33C.glTexImage2D(GL33C.GL_TEXTURE_2D, 0, format, w, h, 0, format, type, data)

		if (mipmap) {
			GL33C.glGenerateMipmap(GL33C.GL_TEXTURE_2D)
		}

		GL33C.glTexParameteri(GL33C.GL_TEXTURE_2D, GL33C.GL_TEXTURE_WRAP_S, wrap)
		GL33C.glTexParameteri(GL33C.GL_TEXTURE_2D, GL33C.GL_TEXTURE_WRAP_T, wrap)
		GL33C.glTexParameteri(GL33C.GL_TEXTURE_2D, GL33C.GL_TEXTURE_MIN_FILTER, minFilter)
		GL33C.glTexParameteri(GL33C.GL_TEXTURE_2D, GL33C.GL_TEXTURE_MAG_FILTER, magFilter)
		GL33C.glBindTexture(GL33C.GL_TEXTURE_2D, 0)

		return texure
	}

	fun createTexture(data: ByteBuffer, w: Int, h: Int, format: Int, type: Int, wrap: Int, mipmap: Boolean, filter: Int): Int {
		return createTexture(data, w, h, format, type, wrap, mipmap, filter, filter)
	}

	fun bindTexture(id: Int) {
		GL33C.glBindTexture(GL33C.GL_TEXTURE_2D, id)
	}
}