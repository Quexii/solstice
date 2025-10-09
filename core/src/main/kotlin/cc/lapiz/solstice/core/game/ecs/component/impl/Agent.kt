package cc.lapiz.solstice.core.game.ecs.component.impl

import cc.lapiz.solstice.core.data.Target
import cc.lapiz.solstice.core.game.level.grid.Grid
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class Agent(var target: Target? = null, @Transient val workingGrid: Grid? = null)