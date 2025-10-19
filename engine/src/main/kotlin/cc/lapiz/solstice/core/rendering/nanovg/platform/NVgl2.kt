package cc.lapiz.solstice.core.rendering.nanovg.platform

import cc.lapiz.solstice.core.rendering.nanovg.NVgl
import org.lwjgl.nanovg.*

class NVgl2 : NVgl {
	override fun create(flags: Int): Long = NanoVGGL2.nvgCreate(flags)
	override fun imageHandle(handle: Long, image: Int): Int = NanoVGGL2.nvglImageHandle(handle, image)
	override fun createImageFromHandle(handle: Long, texture: Int, w: Int, h: Int, flags: Int): Int = NanoVGGL2.nvglCreateImageFromHandle(handle, texture, w, h, flags)
}