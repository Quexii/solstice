package cc.lapiz.solstice.font.stbttf

import cc.lapiz.solstice.data.Vector2
import java.nio.ByteBuffer

object FontUtil {
	fun createSTBTTF(data: ByteBuffer, resolution: Vector2, size: Float, pp: Boolean): STBFontObject {
		val obj = STBFontObject()
		obj.init(data, resolution, size, pp)
		return obj
	}
}