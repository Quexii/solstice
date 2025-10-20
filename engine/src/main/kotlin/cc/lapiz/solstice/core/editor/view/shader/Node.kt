package cc.lapiz.solstice.core.editor.view.shader

import cc.lapiz.solstice.core.data.Vector2

data class Node(
    val id: Int,
    val type: NodeType,
    var pos: Vector2 = Vector2(100f, 100f),
    val inputs: MutableList<Pin> = mutableListOf(),
    val outputs: MutableList<Pin> = mutableListOf(),
    val params: MutableMap<String, Any> = mutableMapOf() // user-editable
)