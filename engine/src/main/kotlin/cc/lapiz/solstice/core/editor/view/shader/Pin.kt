package cc.lapiz.solstice.core.editor.view.shader

data class Pin(
    val id: Int,
    val name: String,
    val valueType: ValueType, // Float, Vec2, Vec3, Vec4, Sampler2D
    val kind: PinKind // INPUT or OUTPUT
)