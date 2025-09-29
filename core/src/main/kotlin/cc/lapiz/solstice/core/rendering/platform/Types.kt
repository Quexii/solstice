package cc.lapiz.solstice.core.rendering.platform

import cc.lapiz.solstice.processor.GenerateGL

@GenerateGL
interface Types {
	val FALSE: Int
	val TRUE: Int

	// Buffer targets
	val ARRAY_BUFFER: Int
	val ELEMENT_ARRAY_BUFFER: Int

	// Buffer usage patterns
	val STATIC_DRAW: Int
	val DYNAMIC_DRAW: Int
	val STREAM_DRAW: Int

	// Data types
	val FLOAT: Int
	val INT: Int
	val UNSIGNED_INT: Int
	val UNSIGNED_BYTE: Int

	// Primitive types
	val TRIANGLES: Int
	val TRIANGLE_FAN: Int
	val TRIANGLE_STRIP: Int
	val LINES: Int
	val LINE_STRIP: Int
	val LINE_LOOP: Int
	val POINTS: Int

	// Polygon modes
	val FILL: Int
	val LINE: Int
	val POINT: Int

	// Flags
	val DEPTH_TEST: Int
	val BLEND: Int
	val CULL_FACE: Int
	val SCISSOR_TEST: Int
	val STENCIL_TEST: Int

	// Blend factors
	val SRC_ALPHA: Int
	val ONE_MINUS_SRC_ALPHA: Int
	val ONE: Int
	val ZERO: Int
	val DST_COLOR: Int
	val ONE_MINUS_DST_COLOR: Int
	val SRC_COLOR: Int
	val ONE_MINUS_SRC_COLOR: Int
	val DST_ALPHA: Int
	val ONE_MINUS_DST_ALPHA: Int

	// Stencil operations
	val KEEP: Int
	val REPLACE: Int
	val INCR: Int
	val DECR: Int
	val INVERT: Int
	val INCR_WRAP: Int
	val DECR_WRAP: Int
	val ALWAYS: Int
	val NEVER: Int
	val EQUAL: Int
	val NOTEQUAL: Int
	val LESS: Int
	val LEQUAL: Int
	val GREATER: Int
	val GEQUAL: Int

	// Face culling
	val FRONT: Int
	val BACK: Int
	val FRONT_AND_BACK: Int
	val CCW: Int
	val CW: Int

	// Texture parameters
	val TEXTURE_2D: Int
	val TEXTURE_MIN_FILTER: Int
	val TEXTURE_MAG_FILTER: Int
	val TEXTURE_WRAP_S: Int
	val TEXTURE_WRAP_T: Int
	val REPEAT: Int
	val CLAMP_TO_EDGE: Int
	val LINEAR: Int
	val NEAREST: Int
	val LINEAR_MIPMAP_LINEAR: Int
	val LINEAR_MIPMAP_NEAREST: Int
	val NEAREST_MIPMAP_LINEAR: Int
	val NEAREST_MIPMAP_NEAREST: Int
	val RGBA: Int
	val RGBA8: Int
	val RGB: Int
	val RGB8: Int

	// Framebuffer attachments
	val COLOR_ATTACHMENT0: Int
	val DEPTH_ATTACHMENT: Int
	val STENCIL_ATTACHMENT: Int
	val DEPTH_STENCIL_ATTACHMENT: Int
	val FRAMEBUFFER: Int
	val RENDERBUFFER: Int
	val FRAMEBUFFER_COMPLETE: Int
	val FRAMEBUFFER_INCOMPLETE_ATTACHMENT: Int
	val FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT: Int
	val FRAMEBUFFER_UNSUPPORTED: Int

	// Shader types
	val VERTEX_SHADER: Int
	val FRAGMENT_SHADER: Int
	val COMPILE_STATUS: Int
	val LINK_STATUS: Int
	val INFO_LOG_LENGTH: Int
	val TEXTURE0: Int

	// Clear bits
	val COLOR_BUFFER_BIT: Int
	val DEPTH_BUFFER_BIT: Int
	val STENCIL_BUFFER_BIT: Int
}