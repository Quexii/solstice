package cc.lapiz.solstice.core.editor.view.shader

data class ShaderGraph(
    val nodes: MutableList<Node> = mutableListOf(),
    val links: MutableList<Link> = mutableListOf(),
    var nextId: Int = 1
)