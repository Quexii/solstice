package cc.lapiz.solstice.core.assets.types

import cc.lapiz.solstice.core.assets.Asset
import cc.lapiz.solstice.core.assets.meta.MetaTexture
import cc.lapiz.solstice.core.rendering.texture.TextureUtil
import cc.lapiz.solstice.core.resource.IO
import org.lwjgl.opengl.GL33C
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer

class Sprite(meta: MetaTexture.Serialized) : Asset(meta) {
	private var data: ByteBuffer? = null
	private var rawData: ByteBuffer? = null
	private var texId = -1
	private var width = 0
	private var height = 0
	private var channels = 0
	override fun load() {
		rawData = IO.getBuffer(meta.path)

		MemoryStack.stackPush().use { stack ->
			val w = stack.mallocInt(1)
			val h = stack.mallocInt(1)
			val c = stack.mallocInt(1)
			data = STBImage.stbi_load_from_memory(rawData!!, w, h, c, 4)
				?: throw RuntimeException("Failed to load image: ${STBImage.stbi_failure_reason()}")
			width = w[0]
			height = h[0]
			channels = c[0]
		}

		texId = TextureUtil.createTexture(
			data!!,
			width,
			height,
			GL33C.GL_RGBA,
			GL33C.GL_UNSIGNED_BYTE,
			when ((meta as MetaTexture.Serialized).wrap) {
				MetaTexture.Serialized.TextureWrap.Clamp -> GL33C.GL_CLAMP_TO_EDGE
				MetaTexture.Serialized.TextureWrap.Repeat -> GL33C.GL_REPEAT
			},
			false,
			when (meta.filter) {
				MetaTexture.Serialized.TextureFilter.Linear -> GL33C.GL_LINEAR
				MetaTexture.Serialized.TextureFilter.Nearest -> GL33C.GL_NEAREST
			}
		)
	}

	fun update(newData: Array<Int>) {
		val type = MetaTexture.Serialized.TextureType.entries[newData[0]]
		val filter = MetaTexture.Serialized.TextureFilter.entries[newData[1]]
		val wrap = MetaTexture.Serialized.TextureWrap.entries[newData[2]]

		TextureUtil.bindTexture(textureId())
		val wrapValue = when (wrap) {
			MetaTexture.Serialized.TextureWrap.Clamp -> GL33C.GL_CLAMP_TO_EDGE
			MetaTexture.Serialized.TextureWrap.Repeat -> GL33C.GL_REPEAT
		}

		val filterValue = when (filter) {
			MetaTexture.Serialized.TextureFilter.Linear -> GL33C.GL_LINEAR
			MetaTexture.Serialized.TextureFilter.Nearest -> GL33C.GL_NEAREST
		}

		GL33C.glTexParameteri(GL33C.GL_TEXTURE_2D, GL33C.GL_TEXTURE_MIN_FILTER, filterValue)
		GL33C.glTexParameteri(GL33C.GL_TEXTURE_2D, GL33C.GL_TEXTURE_MAG_FILTER, filterValue)
		GL33C.glTexParameteri(GL33C.GL_TEXTURE_2D, GL33C.GL_TEXTURE_WRAP_S, wrapValue)
		GL33C.glTexParameteri(GL33C.GL_TEXTURE_2D, GL33C.GL_TEXTURE_WRAP_T, wrapValue)
		TextureUtil.bindTexture(0)
	}

	fun data(): ByteBuffer? = data
	fun rawData(): ByteBuffer? = rawData

	fun textureId(): Int = texId

	fun width(): Int = width
	fun height(): Int = height
	fun channels(): Int = channels
}