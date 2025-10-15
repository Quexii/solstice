package cc.lapiz.solstice.game.components

import cc.lapiz.solstice.game.GameObject

abstract class Component: Behaviours {
    lateinit var gameObject: GameObject
}