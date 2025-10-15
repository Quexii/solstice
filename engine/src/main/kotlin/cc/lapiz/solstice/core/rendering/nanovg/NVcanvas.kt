package cc.lapiz.solstice.rendering.nanovg

import cc.lapiz.solstice.data.Color
import cc.lapiz.solstice.font.FontFace
import cc.lapiz.solstice.font.FontManager
import cc.lapiz.solstice.resource.impl.SpriteId
import org.lwjgl.nanovg.*
import org.lwjgl.system.MemoryUtil
import java.nio.*

@Suppress("FunctionName", "unused") object NVcanvas {
	private lateinit var nvgl: NVgl
	private var context: Long = 0
	private val imagesMap = mutableMapOf<SpriteId, Int>()
	fun init(nvgl: NVgl, flags: Int)  {
		this.nvgl = nvgl
		context = nvgl.create(flags)
	}

	fun initFont(name: String, data: ByteBuffer): Boolean {
		val fontId = NanoVG.nvgCreateFontMem(context, name, data, false)
		return fontId != -1
	}

	fun beginFrame(width: Float, height: Float, devicePixelRatio: Float) {
		NanoVG.nvgBeginFrame(context, width, height, devicePixelRatio)
	}

	fun endFrame() {
		NanoVG.nvgEndFrame(context)
	}

	fun RGBA(r: Byte, g: Byte, b: Byte, a: Byte): NVGColor = NanoVG.nvgRGBA(r, g, b, a, NVGColor.calloc())
	fun RGBAf(r: Float, g: Float, b: Float, a: Float): NVGColor = NanoVG.nvgRGBAf(r, g, b, a, NVGColor.calloc())
	fun RGB(r: Byte, g: Byte, b: Byte): NVGColor = NanoVG.nvgRGB(r, g, b, NVGColor.calloc())
	fun RGBf(r: Float, g: Float, b: Float): NVGColor = NanoVG.nvgRGBf(r, g, b, NVGColor.calloc())
	fun HSL(h: Float, s: Float, l: Float): NVGColor = NanoVG.nvgHSL(h, s, l, NVGColor.calloc())
	fun HEX(hex: Int): NVGColor {
		val r = (hex shr 24 and 0xFF) / 255f
		val g = (hex shr 16 and 0xFF) / 255f
		val b = (hex shr 8 and 0xFF) / 255f
		val a = (hex and 0xFF) / 255f
		return RGBAf(r, g, b, a)
	}

	fun HEX(hex: Long) = HEX(hex.toInt())

	// Exposed NanoVG functions
	fun nvBeginPath() = NanoVG.nvgBeginPath(context)
	fun nvMoveTo(x: Float, y: Float) = NanoVG.nvgMoveTo(context, x, y)
	fun nvLineTo(x: Float, y: Float) = NanoVG.nvgLineTo(context, x, y)
	fun nvBezierTo(c1x: Float, c1y: Float, c2x: Float, c2y: Float, x: Float, y: Float) = NanoVG.nvgBezierTo(context, c1x, c1y, c2x, c2y, x, y)
	fun nvQuadraticTo(cx: Float, cy: Float, x: Float, y: Float) = NanoVG.nvgQuadTo(context, cx, cy, x, y)
	fun nvArcTo(x1: Float, y1: Float, x2: Float, y2: Float, radius: Float) = NanoVG.nvgArcTo(context, x1, y1, x2, y2, radius)
	fun nvClosePath() = NanoVG.nvgClosePath(context)
	fun nvPathWinding(dir: Int) = NanoVG.nvgPathWinding(context, dir)
	fun nvArc(cx: Float, cy: Float, r: Float, a0: Float, a1: Float, dir: Int) = NanoVG.nvgArc(context, cx, cy, r, a0, a1, dir)
	fun nvRect(x: Float, y: Float, w: Float, h: Float) = NanoVG.nvgRect(context, x, y, w, h)
	fun nvRoundedRect(x: Float, y: Float, w: Float, h: Float, r: Float) = NanoVG.nvgRoundedRect(context, x, y, w, h, r)
	fun nvRoundedRectVarying(x: Float, y: Float, w: Float, h: Float, radTopLeft: Float, radTopRight: Float, radBottomRight: Float, radBottomLeft: Float) = NanoVG.nvgRoundedRectVarying(context, x, y, w, h, radTopLeft, radTopRight, radBottomRight, radBottomLeft)
	fun nvEllipse(cx: Float, cy: Float, rx: Float, ry: Float) = NanoVG.nvgEllipse(context, cx, cy, rx, ry)
	fun nvCircle(cx: Float, cy: Float, r: Float) = NanoVG.nvgCircle(context, cx, cy, r)
	fun nvFill() = NanoVG.nvgFill(context)
	fun nvStroke() = NanoVG.nvgStroke(context)
	fun nvFillPaint(paint: NVGPaint) = NanoVG.nvgFillPaint(context, paint)
	fun nvStrokePaint(paint: NVGPaint) = NanoVG.nvgStrokePaint(context, paint)
	fun nvStrokeColor(color: NVGColor) = NanoVG.nvgStrokeColor(context, color)
	fun nvFillColor(color: NVGColor) = NanoVG.nvgFillColor(context, color)
	fun nvStrokeWidth(width: Float) = NanoVG.nvgStrokeWidth(context, width)
	fun nvMiterLimit(limit: Float) = NanoVG.nvgMiterLimit(context, limit)
	fun nvLineCap(cap: Int) = NanoVG.nvgLineCap(context, cap)
	fun nvLineJoin(join: Int) = NanoVG.nvgLineJoin(context, join)
	fun nvGlobalAlpha(alpha: Float) = NanoVG.nvgGlobalAlpha(context, alpha)
	fun nvReset() = NanoVG.nvgReset(context)
	fun nvCreateImage(file: String, flags: Int): Int = NanoVG.nvgCreateImage(context, file, flags)
	fun nvCreateImageMem(data: ByteBuffer, flags: Int): Int = NanoVG.nvgCreateImageMem(context, flags, data)
	fun nvUpdateImage(image: Int, data: ByteBuffer) = NanoVG.nvgUpdateImage(context, image, data)
	fun nvImageSize(image: Int, w: IntBuffer, h: IntBuffer) = NanoVG.nvgImageSize(context, image, w, h)
	fun nvDeleteImage(image: Int) = NanoVG.nvgDeleteImage(context, image)
	fun nvCreatePaint(): NVGPaint = NVGPaint.calloc()
	fun nvLinearGradient(sx: Float, sy: Float, ex: Float, ey: Float, icol: NVGColor, ocol: NVGColor): NVGPaint = NanoVG.nvgLinearGradient(context, sx, sy, ex, ey, icol, ocol, NVGPaint.calloc())
	fun nvBoxGradient(x: Float, y: Float, w: Float, h: Float, r: Float, feather: Float, icol: NVGColor, ocol: NVGColor): NVGPaint = NanoVG.nvgBoxGradient(context, x, y, w, h, r, feather, icol, ocol, NVGPaint.calloc())
	fun nvRadialGradient(cx: Float, cy: Float, inr: Float, outr: Float, icol: NVGColor, ocol: NVGColor): NVGPaint = NanoVG.nvgRadialGradient(context, cx, cy, inr, outr, icol, ocol, NVGPaint.calloc())
	fun nvImagePattern(x: Float, y: Float, w: Float, h: Float, angle: Float, image: Int, alpha: Float): NVGPaint = NanoVG.nvgImagePattern(context, x, y, w, h, angle, image, alpha, NVGPaint.calloc())
	fun nvSave() = NanoVG.nvgSave(context)
	fun nvRestore() = NanoVG.nvgRestore(context)
	fun nvResetTransform() = NanoVG.nvgResetTransform(context)
	fun nvTransform(a: Float, b: Float, c: Float, d: Float, e: Float, f: Float) = NanoVG.nvgTransform(context, a, b, c, d, e, f)
	fun nvTranslate(x: Float, y: Float) = NanoVG.nvgTranslate(context, x, y)
	fun nvRotate(angle: Float) = NanoVG.nvgRotate(context, angle)
	fun nvSkewX(angle: Float) = NanoVG.nvgSkewX(context, angle)
	fun nvSkewY(angle: Float) = NanoVG.nvgSkewY(context, angle)
	fun nvScale(x: Float, y: Float) = NanoVG.nvgScale(context, x, y)
	fun nvCurrentTransform(xform: FloatBuffer) = NanoVG.nvgCurrentTransform(context, xform)
	fun nvTransformIdentity(dst: FloatBuffer) = NanoVG.nvgTransformIdentity(dst)
	fun nvTransformTranslate(dst: FloatBuffer, tx: Float, ty: Float) = NanoVG.nvgTransformTranslate(dst, tx, ty)
	fun nvTransformScale(dst: FloatBuffer, sx: Float, sy: Float) = NanoVG.nvgTransformScale(dst, sx, sy)
	fun nvTransformRotate(dst: FloatBuffer, a: Float) = NanoVG.nvgTransformRotate(dst, a)
	fun nvTransformSkewX(dst: FloatBuffer, a: Float) = NanoVG.nvgTransformSkewX(dst, a)
	fun nvTransformSkewY(dst: FloatBuffer, a: Float) = NanoVG.nvgTransformSkewY(dst, a)
	fun nvTransformMultiply(dst: FloatBuffer, src: FloatBuffer) = NanoVG.nvgTransformMultiply(dst, src)
	fun nvTransformPremultiply(dst: FloatBuffer, src: FloatBuffer) = NanoVG.nvgTransformPremultiply(dst, src)
	fun nvTextAlign(align: Int) = NanoVG.nvgTextAlign(context, align)
	fun nvFontSize(size: Float) = NanoVG.nvgFontSize(context, size)
	fun nvFontBlur(blur: Float) = NanoVG.nvgFontBlur(context, blur)
	fun nvTextLetterSpacing(spacing: Float) = NanoVG.nvgTextLetterSpacing(context, spacing)
	fun nvTextLineHeight(lineHeight: Float) = NanoVG.nvgTextLineHeight(context, lineHeight)
	fun nvTextFontFace(font: String) = NanoVG.nvgFontFace(context, font)
	fun nvTextFontFaceId(fontId: Int) = NanoVG.nvgFontFaceId(context, fontId)
	fun nvText(text: String) = NanoVG.nvgText(context, 0f, 0f, text)
	fun nvText(x: Float, y: Float, text: String) = NanoVG.nvgText(context, x, y, text)
	fun nvTextBox(x: Float, y: Float, breakRowWidth: Float, text: String) = NanoVG.nvgTextBox(context, x, y, breakRowWidth, text)
	fun nvTextBounds(text: String, bounds: FloatBuffer) = NanoVG.nvgTextBounds(context, 0f, 0f, text, bounds)
	fun nvTextBounds(x: Float, y: Float, text: String, bounds: FloatBuffer) = NanoVG.nvgTextBounds(context, x, y, text, bounds)
	fun nvTextBoxBounds(x: Float, y: Float, breakRowWidth: Float, text: String, bounds: FloatBuffer) = NanoVG.nvgTextBoxBounds(context, x, y, breakRowWidth, text, bounds)
	fun nvLerpRGBA(c0: NVGColor, c1: NVGColor, u: Float, dest: NVGColor) = NanoVG.nvgLerpRGBA(c0, c1, u, dest)

	fun rect(x: Float, y: Float, w: Float, h: Float, color: Color) {
		nvBeginPath()
		nvRect(x, y, w, h)
		nvFillColor(color.toNanoVG())
		nvFill()
	}

	fun rect(x: Float, y: Float, w: Float, h: Float, color: NVGPaint) {
		nvBeginPath()
		nvRect(x, y, w, h)
		nvFillPaint(color)
		nvFill()
	}

	fun strokeRect(x: Float, y: Float, w: Float, h: Float, color: Color, strokeWidth: Float) {
		nvBeginPath()
		nvRect(x, y, w, h)
		nvStrokeColor(color.toNanoVG())
		nvStrokeWidth(strokeWidth)
		nvStroke()
	}

	fun strokeRect(x: Float, y: Float, w: Float, h: Float, color: NVGPaint, strokeWidth: Float) {
		nvBeginPath()
		nvRect(x, y, w, h)
		nvStrokePaint(color)
		nvStrokeWidth(strokeWidth)
		nvStroke()
	}

	fun roundedRect(x: Float, y: Float, w: Float, h: Float, r: Float, color: Color) {
		nvBeginPath()
		nvRoundedRect(x, y, w, h, r)
		nvFillColor(color.toNanoVG())
		nvFill()
	}

	fun roundedRect(x: Float, y: Float, w: Float, h: Float, r: Float, color: NVGPaint) {
		nvBeginPath()
		nvRoundedRect(x, y, w, h, r)
		nvFillPaint(color)
		nvFill()
	}

	fun strokeRoundedRect(x: Float, y: Float, w: Float, h: Float, r: Float, color: Color, strokeWidth: Float) {
		nvBeginPath()
		nvRoundedRect(x, y, w, h, r)
		nvStrokeColor(color.toNanoVG())
		nvStrokeWidth(strokeWidth)
		nvStroke()
	}

	fun strokeRoundedRect(x: Float, y: Float, w: Float, h: Float, r: Float, color: NVGPaint, strokeWidth: Float) {
		nvBeginPath()
		nvRoundedRect(x, y, w, h, r)
		nvStrokePaint(color)
		nvStrokeWidth(strokeWidth)
		nvStroke()
	}

	fun text(x: Float, y: Float, text: String, size: Float, blur: Float, color: Color, face: FontFace = FontManager.Default.Default, textAlign: TextAlign = TextAlign.LeftTop) {
		nvFontSize(size)
		nvFontBlur(blur)
		nvTextAlign(textAlign.value)
		nvFillColor(color.toNanoVG())
		nvTextFontFace(face.id)
		nvText(x, y, text)
	}

	fun text(x: Float, y: Float, text: String, color: Color, size: Float = 21f, face: FontFace = FontManager.Default.Default, textAlign: TextAlign = TextAlign.LeftTop) {
		text(x, y, text, size, 0f, color, face, textAlign)
	}

	fun stringSize(text: String, size: Float, face: FontFace = FontManager.Default.Default): Float {
		nvFontSize(size)
		nvTextAlign(TextAlign.LeftTop.value)
		nvTextFontFace(face.id)
		val buf = MemoryUtil.memAllocFloat(4)
		nvTextBounds(text, buf)
		val width = buf[2] - buf[0]
		MemoryUtil.memFree(buf)
		return width
	}

	fun strokeLine(x0: Float, y0: Float, x1: Float, y1: Float, color: Color, strokeWidth: Float) {
		nvBeginPath()
		nvMoveTo(x0, y0)
		nvLineTo(x1, y1)
		nvStrokeColor(color.toNanoVG())
		nvStrokeWidth(strokeWidth)
		nvStroke()
	}

	fun circle(cx: Float, cy: Float, r: Float, color: Color) {
		nvBeginPath()
		nvCircle(cx, cy, r)
		nvFillColor(color.toNanoVG())
		nvFill()
	}

	fun sprite(x: Float, y: Float, w: Float, h: Float, sprite: SpriteId) {
		if (!imagesMap.contains(sprite)) {
			imagesMap[sprite] = nvCreateImageMem(sprite.rawData()!!, NanoVG.NVG_IMAGE_NEAREST)
		}

		val paint = nvImagePattern(x, y, w, h, 0f, imagesMap[sprite]!!, 1f)
		nvBeginPath()
		nvRect(x, y, w, h)
		nvFillPaint(paint)
		nvFill()
		paint.free()
	}
}