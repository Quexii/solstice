package cc.lapiz.solstice.rendering.nanovg

interface NVgl {
	fun create(flags: Int): Long
	fun imageHandle(handle: Long, image: Int): Int
	fun createImageFromHandle(handle: Long, texture: Int, w: Int, h: Int, flags: Int): Int
}