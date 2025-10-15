package cc.lapiz.solstice.font.stbttf

import cc.lapiz.solstice.data.*
import cc.lapiz.solstice.rendering.texture.*
import org.lwjgl.*
import org.lwjgl.opengl.*
import org.lwjgl.stb.*
import org.lwjgl.system.*
import java.nio.*

private typealias stbtt = STBTruetype

private const val DEFAULT_CHARSET = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~"

class STBFontObject {
	private var texture: Int = -1
	private var fontData: ByteBuffer? = null
	private var bitmap: ByteBuffer? = null
	private lateinit var fontInfo: STBTTFontinfo
	private var scale: Float = 0f
	private var ascent: Float = 0f
	private var descent: Float = 0f
	private var lineGap: Float = 0f
	private var lineHeight: Float = 0f
	private var pixelPerfect: Boolean = false

	private val glyphCache = mutableMapOf<Char, GlyphInfo>()

	fun init(data: ByteBuffer, resolution: Vector2, size: Float, pixelPerfect: Boolean) {
		fontData = data
		this.pixelPerfect = pixelPerfect
		fontInfo = STBTTFontinfo.create()
		if (!STBTruetype.stbtt_InitFont(fontInfo, fontData!!)) {
			throw RuntimeException("Failed to init font")
		}

		scale = STBTruetype.stbtt_ScaleForPixelHeight(fontInfo, size)

		MemoryStack.stackPush().use { stack ->
			val ascentBuf = stack.mallocInt(1)
			val descentBuf = stack.mallocInt(1)
			val lineGapBuf = stack.mallocInt(1)
			STBTruetype.stbtt_GetFontVMetrics(fontInfo, ascentBuf, descentBuf, lineGapBuf)
			ascent = ascentBuf.get(0) * scale
			descent = descentBuf.get(0) * scale
			lineGap = lineGapBuf.get(0) * scale
		}

		lineHeight = ascent - descent + lineGap

		buildAtlas(resolution)
	}

	private fun buildAtlas(resolution: Vector2) {
		val cdata = STBTTBakedChar.calloc(DEFAULT_CHARSET.toSortedSet().last().code)
		bitmap = BufferUtils.createByteBuffer((resolution.x * resolution.y).toInt())
		stbtt.stbtt_BakeFontBitmap(fontData!!, scale, bitmap!!, resolution.x.toInt(), resolution.y.toInt(), DEFAULT_CHARSET.toSortedSet().first().code, cdata)
		texture = TextureUtil.createTexture(bitmap!!, resolution.x.toInt(), resolution.y.toInt(), GL33C.GL_RED, GL33C.GL_UNSIGNED_BYTE, GL33C.GL_CLAMP_TO_EDGE, !pixelPerfect, if (pixelPerfect) GL33C.GL_NEAREST else GL33C.GL_LINEAR)
		DEFAULT_CHARSET.forEachIndexed { i, c ->
			val bc = cdata[i]
			glyphCache[c]?.texCoords?.copy(
				u0 = bc.x0().toFloat(),
				v0 = bc.y0().toFloat(),
				u1 = bc.x1().toFloat(),
				v1 = bc.y1().toFloat(),
			)
		}
	}

	fun getGlyph(char: Char): GlyphInfo {
		return glyphCache.getOrPut(char) { genGlyph(char) }
	}

	private fun genGlyph(char: Char): GlyphInfo {
		val cp = char.code
		var advanceWidth = 0
		var leftSideBearing = 0
		var xOff = 0
		var yOff = 0
		var width = 0
		var height = 0

		MemoryStack.stackPush().use { stack ->
			val advanceWidthBuf = stack.mallocInt(1)
			val leftSideBearingBuf = stack.mallocInt(1)
			val x0Buf = stack.mallocInt(1)
			val y0Buf = stack.mallocInt(1)
			val x1Buf = stack.mallocInt(1)
			val y1Buf = stack.mallocInt(1)
			stbtt.stbtt_GetCodepointHMetrics(fontInfo, cp, advanceWidthBuf, leftSideBearingBuf)
			advanceWidth = advanceWidthBuf.get(0)
			leftSideBearing = leftSideBearingBuf.get(0)

			stbtt.stbtt_GetCodepointBitmapBox(fontInfo, cp, scale, scale, x0Buf, y1Buf, x1Buf, y1Buf)

			xOff = x0Buf.get(0)
			yOff = y0Buf.get(0)
			width = x1Buf.get(0) - xOff
			height = y1Buf.get(0) - yOff
		}

		val bitmap = if (width > 0 && height > 0) {
			val buffer = MemoryUtil.memAlloc(width * height)
			STBTruetype.stbtt_MakeCodepointBitmap(
				fontInfo, buffer, width, height, width, scale, scale, cp
			)
			buffer
		} else {
			null
		}

		return GlyphInfo(
			advanceWidth = advanceWidth, leftSideBearing = leftSideBearing, bitmap = bitmap, width = width, height = height, xOffset = xOff, yOffset = yOff
		)
	}

	fun getTextWidth(text: String): Float {
		var width = 0f
		for (i in text.indices) {
			val glyphInfo = getGlyph(text[i])
			width += glyphInfo.advanceWidth

			if (i < text.length - 1) {
				val kern = STBTruetype.stbtt_GetCodepointKernAdvance(
					fontInfo, text[i].code, text[i + 1].code
				)
				width += kern * scale
			}
		}
		return width
	}

	data class GlyphInfo(
		val advanceWidth: Int,
		val leftSideBearing: Int,
		val bitmap: ByteBuffer?,
		val width: Int,
		val height: Int,
		val xOffset: Int,
		val yOffset: Int,
		val texCoords: UVRect = UVRect(),
	)
}