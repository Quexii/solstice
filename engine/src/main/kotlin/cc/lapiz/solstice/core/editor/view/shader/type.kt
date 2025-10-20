package cc.lapiz.solstice.core.editor.view.shader

enum class ValueType { Float, Vec2, Vec3, Vec4, Sampler2D, Bool }
enum class NodeType {
    // Sources
    Time, UV, Constant, Color, Texture2D,
    // Math
    Add, Sub, Mul, Div, Dot, Normalize, Length, Sin, Cos, Pow, Clamp, Lerp, Combine, Split,
    // Surface output
    FragmentOutput // final color
}