package cc.lapiz.solstice.core.game.components.meta

import cc.lapiz.solstice.core.game.components.Component
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class RequiredComponents(vararg val components: KClass<out Component>)