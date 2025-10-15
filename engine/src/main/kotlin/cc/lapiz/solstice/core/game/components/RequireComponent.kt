package cc.lapiz.solstice.core.game.components

import cc.lapiz.solstice.game.components.Component
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class RequireComponent(vararg val components: KClass<out Component>)