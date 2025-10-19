package cc.lapiz.solstice.core.game.components

import cc.lapiz.solstice.core.game.GameObject
import cc.lapiz.solstice.core.game.components.meta.ComponentName
import cc.lapiz.solstice.core.utils.getAnnotations

abstract class Component private constructor(private var internalName: String): Behaviours {
    constructor(): this("") {
        internalName = (getAnnotations(this::class).find { it is ComponentName } as ComponentName?)?.name ?: this::class.simpleName ?: ""
    }

    val name: String
        get() = internalName
    lateinit var gameObject: GameObject
}