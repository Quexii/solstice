package cc.lapiz.solstice.core.resource.impl

import cc.lapiz.solstice.core.rendering.platform.Graphics
import cc.lapiz.solstice.core.resource.IO
import cc.lapiz.solstice.core.resource.Loadable
import cc.lapiz.solstice.core.resource.Resource
import cc.lapiz.solstice.core.utils.logger
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer

class SpriteResource(override val id: String, override val name: String, override val path: String) : Resource, Loadable {
	private var data: ByteBuffer? = null
	private var texId = -1
	private var width = 0
	private var height = 0
	private var channels = 0

	private val LOGGER = logger(SpriteResource::class.java)

	override fun load() {
		val rawData = IO.getBuffer(path)
		texId = Graphics.genTextures()

		MemoryStack.stackPush().use { stack ->
			val w = stack.mallocInt(1)
			val h = stack.mallocInt(1)
			val c = stack.mallocInt(1)
			data = STBImage.stbi_load_from_memory(rawData, w, h, c, 4) ?: throw RuntimeException("Failed to load image: ${STBImage.stbi_failure_reason()}")
			width = w[0]
			height = h[0]
			channels = c[0]
		}

		Graphics.bindTexture(Graphics.TEXTURE_2D, texId)
		Graphics.texParameteri(Graphics.TEXTURE_2D, Graphics.TEXTURE_MIN_FILTER, Graphics.NEAREST)
		Graphics.texParameteri(Graphics.TEXTURE_2D, Graphics.TEXTURE_MAG_FILTER, Graphics.NEAREST)
		Graphics.texParameteri(Graphics.TEXTURE_2D, Graphics.TEXTURE_WRAP_S, Graphics.CLAMP_TO_EDGE)
		Graphics.texParameteri(Graphics.TEXTURE_2D, Graphics.TEXTURE_WRAP_T, Graphics.CLAMP_TO_EDGE)
		Graphics.texImage2D(
			Graphics.TEXTURE_2D, 0, Graphics.RGBA8,
			width, height, 0, Graphics.RGBA, Graphics.UNSIGNED_BYTE, data!!
		)
		Graphics.bindTexture(Graphics.TEXTURE_2D, 0)

		LOGGER.info("Loaded sprite '$name' ($width x $height, $channels channels)")
	}

	fun data(): ByteBuffer? = data

	fun textureId(): Int = texId
}