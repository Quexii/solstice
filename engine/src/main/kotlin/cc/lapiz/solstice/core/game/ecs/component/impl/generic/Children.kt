package cc.lapiz.solstice.game.ecs.component.impl.generic

import kotlinx.serialization.Serializable

@Serializable
class Children(var entities: MutableList<Int> = mutableListOf())