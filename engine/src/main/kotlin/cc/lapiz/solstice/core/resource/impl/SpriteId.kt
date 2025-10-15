package cc.lapiz.solstice.resource.impl

import cc.lapiz.solstice.rendering.texture.TextureUtil
import cc.lapiz.solstice.resource.IO
import cc.lapiz.solstice.resource.Loadable
import cc.lapiz.solstice.resource.Id
import cc.lapiz.solstice.utils.logger
import org.lwjgl.opengl.GL33C
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer

class SpriteId(override val id: String, override val path: String) : Id, Loadable {
	private var data: ByteBuffer? = null
	private var rawData: ByteBuffer? = null
	private var texId = -1
	private var width = 0
	private var height = 0
	private var channels = 0

	private val LOGGER = logger(SpriteId::class.java)

	override fun load() {
		rawData = IO.getBuffer(path)

		MemoryStack.stackPush().use { stack ->
			val w = stack.mallocInt(1)
			val h = stack.mallocInt(1)
			val c = stack.mallocInt(1)
			data = STBImage.stbi_load_from_memory(rawData!!, w, h, c, 4) ?: throw RuntimeException("Failed to load image: ${STBImage.stbi_failure_reason()}")
			width = w[0]
			height = h[0]
			channels = c[0]
		}

		texId = TextureUtil.createTexture(data!!, width, height, GL33C.GL_RGBA, GL33C.GL_UNSIGNED_BYTE, GL33C.GL_CLAMP_TO_EDGE, false, GL33C.GL_NEAREST)

		LOGGER.info("Loaded sprite '$id' ($width x $height, $channels channels)")
	}

	fun data(): ByteBuffer? = data
	fun rawData(): ByteBuffer? = rawData

	fun textureId(): Int = texId

	fun width(): Int = width
	fun height(): Int = height
	fun channels(): Int = channels
}