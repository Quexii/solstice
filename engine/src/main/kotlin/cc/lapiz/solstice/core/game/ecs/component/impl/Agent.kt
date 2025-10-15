package cc.lapiz.solstice.game.ecs.component.impl

import cc.lapiz.solstice.data.Target
import cc.lapiz.solstice.game.level.grid.Grid
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class Agent(var target: Target? = null, @Transient val workingGrid: Grid? = null)