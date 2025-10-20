package cc.lapiz.solstice.core.editor.view.shader

data class Link(val id: Int, val fromPin: Int, val toPin: Int)
enum class PinKind { INPUT, OUTPUT }