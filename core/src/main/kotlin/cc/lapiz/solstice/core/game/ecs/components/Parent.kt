package cc.lapiz.solstice.core.game.ecs.components

data class Parent(val parentId: Int)
data class Children(val childIds: MutableList<Int> = mutableListOf())
