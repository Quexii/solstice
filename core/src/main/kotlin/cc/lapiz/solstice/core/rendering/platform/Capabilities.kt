package cc.lapiz.solstice.core.rendering.platform

import cc.lapiz.solstice.processor.GenerateGL

@GenerateGL
interface Capabilities {
	val supportsInstancing: Boolean
	val supportsVAO: Boolean
	val maxTextureUnits: Int
	val maxVertexAttributes: Int
}