package cc.lapiz.solstice.desktop.rendering.paltform

import cc.lapiz.solstice.core.rendering.platform.Types
import org.lwjgl.opengl.GL33C

class GlTypes : Types {
	override val FALSE: Int
		get() = GL33C.GL_FALSE
	override val TRUE: Int
		get() = GL33C.GL_TRUE
	override val ARRAY_BUFFER: Int
		get() = GL33C.GL_ARRAY_BUFFER
	override val ELEMENT_ARRAY_BUFFER: Int
		get() = GL33C.GL_ELEMENT_ARRAY_BUFFER
	override val STATIC_DRAW: Int
		get() = GL33C.GL_STATIC_DRAW
	override val DYNAMIC_DRAW: Int
		get() = GL33C.GL_DYNAMIC_DRAW
	override val STREAM_DRAW: Int
		get() = GL33C.GL_STREAM_DRAW
	override val FLOAT: Int
		get() = GL33C.GL_FLOAT
	override val INT: Int
		get() = GL33C.GL_INT
	override val UNSIGNED_INT: Int
		get() = GL33C.GL_UNSIGNED_INT
	override val UNSIGNED_BYTE: Int
		get() = GL33C.GL_UNSIGNED_BYTE
	override val TRIANGLES: Int
		get() = GL33C.GL_TRIANGLES
	override val TRIANGLE_STRIP: Int
		get() = GL33C.GL_TRIANGLE_STRIP
	override val LINES: Int
		get() = GL33C.GL_LINES
	override val LINE_STRIP: Int
		get() = GL33C.GL_LINE_STRIP
	override val POINTS: Int
		get() = GL33C.GL_POINTS
	override val DEPTH_TEST: Int
		get() = GL33C.GL_DEPTH_TEST
	override val BLEND: Int
		get() = GL33C.GL_BLEND
	override val CULL_FACE: Int
		get() = GL33C.GL_CULL_FACE
	override val SCISSOR_TEST: Int
		get() = GL33C.GL_SCISSOR_TEST
	override val STENCIL_TEST: Int
		get() = GL33C.GL_STENCIL_TEST
	override val SRC_ALPHA: Int
		get() = GL33C.GL_SRC_ALPHA
	override val ONE_MINUS_SRC_ALPHA: Int
		get() = GL33C.GL_ONE_MINUS_SRC_ALPHA
	override val ONE: Int
		get() = GL33C.GL_ONE
	override val ZERO: Int
		get() = GL33C.GL_ZERO
	override val DST_COLOR: Int
		get() = GL33C.GL_DST_COLOR
	override val ONE_MINUS_DST_COLOR: Int
		get() = GL33C.GL_ONE_MINUS_DST_COLOR
	override val SRC_COLOR: Int
		get() = GL33C.GL_SRC_COLOR
	override val ONE_MINUS_SRC_COLOR: Int
		get() = GL33C.GL_ONE_MINUS_SRC_COLOR
	override val DST_ALPHA: Int
		get() = GL33C.GL_DST_ALPHA
	override val ONE_MINUS_DST_ALPHA: Int
		get() = GL33C.GL_ONE_MINUS_DST_ALPHA
	override val KEEP: Int
		get() = GL33C.GL_KEEP
	override val REPLACE: Int
		get() = GL33C.GL_REPLACE
	override val INCR: Int
		get() = GL33C.GL_INCR
	override val DECR: Int
		get() = GL33C.GL_DECR
	override val INVERT: Int
		get() = GL33C.GL_INVERT
	override val INCR_WRAP: Int
		get() = GL33C.GL_INCR_WRAP
	override val DECR_WRAP: Int
		get() = GL33C.GL_DECR_WRAP
	override val ALWAYS: Int
		get() = GL33C.GL_ALWAYS
	override val NEVER: Int
		get() = GL33C.GL_NEVER
	override val EQUAL: Int
		get() = GL33C.GL_EQUAL
	override val NOTEQUAL: Int
		get() = GL33C.GL_NOTEQUAL
	override val LESS: Int
		get() = GL33C.GL_LESS
	override val LEQUAL: Int
		get() = GL33C.GL_LEQUAL
	override val GREATER: Int
		get() = GL33C.GL_GREATER
	override val GEQUAL: Int
		get() = GL33C.GL_GEQUAL
	override val FRONT: Int
		get() = GL33C.GL_FRONT
	override val BACK: Int
		get() = GL33C.GL_BACK
	override val FRONT_AND_BACK: Int
		get() = GL33C.GL_FRONT_AND_BACK
	override val CCW: Int
		get() = GL33C.GL_CCW
	override val CW: Int
		get() = GL33C.GL_CW
	override val TEXTURE_2D: Int
		get() = GL33C.GL_TEXTURE_2D
	override val TEXTURE_MIN_FILTER: Int
		get() = GL33C.GL_TEXTURE_MIN_FILTER
	override val TEXTURE_MAG_FILTER: Int
		get() = GL33C.GL_TEXTURE_MAG_FILTER
	override val TEXTURE_WRAP_S: Int
		get() = GL33C.GL_TEXTURE_WRAP_S
	override val TEXTURE_WRAP_T: Int
		get() = GL33C.GL_TEXTURE_WRAP_T
	override val REPEAT: Int
		get() = GL33C.GL_REPEAT
	override val CLAMP_TO_EDGE: Int
		get() = GL33C.GL_CLAMP_TO_EDGE
	override val LINEAR: Int
		get() = GL33C.GL_LINEAR
	override val NEAREST: Int
		get() = GL33C.GL_NEAREST
	override val LINEAR_MIPMAP_LINEAR: Int
		get() = GL33C.GL_LINEAR_MIPMAP_LINEAR
	override val LINEAR_MIPMAP_NEAREST: Int
		get() = GL33C.GL_LINEAR_MIPMAP_NEAREST
	override val NEAREST_MIPMAP_LINEAR: Int
		get() = GL33C.GL_NEAREST_MIPMAP_LINEAR
	override val NEAREST_MIPMAP_NEAREST: Int
		get() = GL33C.GL_NEAREST_MIPMAP_NEAREST
	override val RGBA: Int
		get() = GL33C.GL_RGBA
	override val RGB: Int
		get() = GL33C.GL_RGB
	override val COLOR_ATTACHMENT0: Int
		get() = GL33C.GL_COLOR_ATTACHMENT0
	override val DEPTH_ATTACHMENT: Int
		get() = GL33C.GL_DEPTH_ATTACHMENT
	override val STENCIL_ATTACHMENT: Int
		get() = GL33C.GL_STENCIL_ATTACHMENT
	override val DEPTH_STENCIL_ATTACHMENT: Int
		get() = GL33C.GL_DEPTH_STENCIL_ATTACHMENT
	override val FRAMEBUFFER: Int
		get() = GL33C.GL_FRAMEBUFFER
	override val RENDERBUFFER: Int
		get() = GL33C.GL_RENDERBUFFER
	override val FRAMEBUFFER_COMPLETE: Int
		get() = GL33C.GL_FRAMEBUFFER_COMPLETE
	override val FRAMEBUFFER_INCOMPLETE_ATTACHMENT: Int
		get() = GL33C.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT
	override val FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT: Int
		get() = GL33C.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT
	override val FRAMEBUFFER_UNSUPPORTED: Int
		get() = GL33C.GL_FRAMEBUFFER_UNSUPPORTED
	override val VERTEX_SHADER: Int
		get() = GL33C.GL_VERTEX_SHADER
	override val FRAGMENT_SHADER: Int
		get() = GL33C.GL_FRAGMENT_SHADER
	override val COMPILE_STATUS: Int
		get() = GL33C.GL_COMPILE_STATUS
	override val LINK_STATUS: Int
		get() = GL33C.GL_LINK_STATUS
	override val INFO_LOG_LENGTH: Int
		get() = GL33C.GL_INFO_LOG_LENGTH
	override val TEXTURE0: Int
		get() = GL33C.GL_TEXTURE0
	override val COLOR_BUFFER_BIT: Int
		get() = GL33C.GL_COLOR_BUFFER_BIT
	override val DEPTH_BUFFER_BIT: Int
		get() = GL33C.GL_DEPTH_BUFFER_BIT
	override val STENCIL_BUFFER_BIT: Int
		get() = GL33C.GL_STENCIL_BUFFER_BIT
}