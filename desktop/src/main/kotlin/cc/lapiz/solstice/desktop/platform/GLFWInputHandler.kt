package cc.lapiz.solstice.desktop.platform

import cc.lapiz.solstice.core.*
import cc.lapiz.solstice.core.input.InputHandler
import cc.lapiz.solstice.core.event.*
import cc.lapiz.solstice.core.input.*
import cc.lapiz.solstice.desktop.window.*
import org.joml.*
import org.lwjgl.glfw.GLFW.*

class GLFWInputHandler(window: Window) : InputHandler {
	private val keyStates = mutableSetOf<Key>()
	private val mouseButtons = mutableSetOf<MouseButton>()
	private var mousePos = Vector2f()

	override fun isKeyPressed(key: Key): Boolean {
		return key in keyStates
	}

	override fun isMouseButtonPressed(button: MouseButton): Boolean {
		return button in mouseButtons
	}

	override fun getMousePosition(): Vector2f {
		return mousePos
	}

	init {
		val handle = window.handle

		glfwSetKeyCallback(handle) { _, key, _, action, _ ->
			val k = translateKeyGLFW(key)
			if (k != null) {
				when (action) {
					GLFW_PRESS -> keyStates.add(k)
					GLFW_RELEASE -> keyStates.remove(k)
				}

				val event = when (action) {
					GLFW_PRESS -> InputEvent.KeyPress(k)
					GLFW_RELEASE -> InputEvent.KeyRelease(k)
					else -> null
				}

				event?.let { GameCore.EventQueue.push(it) }
			}
		}

		glfwSetCharCallback(handle) { _, codepoint ->
			val char = codepoint.toChar()
			GameCore.EventQueue.push(InputEvent.KeyType(char))
		}

		glfwSetCursorPosCallback(handle) { _, xpos, ypos ->
			mousePos.set(xpos.toFloat(), ypos.toFloat())
			GameCore.EventQueue.push(InputEvent.MouseMove(mousePos))
		}

		glfwSetMouseButtonCallback(handle) { _, button, action, _ ->
			val mb = when (button) {
				GLFW_MOUSE_BUTTON_LEFT -> MouseButton.LEFT
				GLFW_MOUSE_BUTTON_RIGHT -> MouseButton.RIGHT
				GLFW_MOUSE_BUTTON_MIDDLE -> MouseButton.MIDDLE
				else -> null
			}

			if (mb != null) {
				when (action) {
					GLFW_PRESS -> mouseButtons.add(mb)
					GLFW_RELEASE -> mouseButtons.remove(mb)
				}

				val event = when (action) {
					GLFW_PRESS -> InputEvent.MousePress(mb)
					GLFW_RELEASE -> InputEvent.MouseRelease(mb)
					else -> null
				}

				event?.let { GameCore.EventQueue.push(it) }
			}
		}

		glfwSetScrollCallback(handle) { _, xpos, ypos ->
			GameCore.EventQueue.push(InputEvent.MouseScroll(ypos.toFloat()))
		}
	}

	companion object {
		private fun translateKeyGLFW(key: Int): Key? {
			return when (key) {
				GLFW_KEY_SPACE -> Keys.SPACE
				GLFW_KEY_APOSTROPHE -> Keys.APOSTROPHE
				GLFW_KEY_COMMA -> Keys.COMMA
				GLFW_KEY_MINUS -> Keys.MINUS
				GLFW_KEY_PERIOD -> Keys.PERIOD
				GLFW_KEY_SLASH -> Keys.SLASH
				GLFW_KEY_0 -> Keys.NUM_0
				GLFW_KEY_1 -> Keys.NUM_1
				GLFW_KEY_2 -> Keys.NUM_2
				GLFW_KEY_3 -> Keys.NUM_3
				GLFW_KEY_4 -> Keys.NUM_4
				GLFW_KEY_5 -> Keys.NUM_5
				GLFW_KEY_6 -> Keys.NUM_6
				GLFW_KEY_7 -> Keys.NUM_7
				GLFW_KEY_8 -> Keys.NUM_8
				GLFW_KEY_9 -> Keys.NUM_9
				GLFW_KEY_SEMICOLON -> Keys.SEMICOLON
				GLFW_KEY_EQUAL -> Keys.EQUAL
				GLFW_KEY_A -> Keys.A
				GLFW_KEY_B -> Keys.B
				GLFW_KEY_C -> Keys.C
				GLFW_KEY_D -> Keys.D
				GLFW_KEY_E -> Keys.E
				GLFW_KEY_F -> Keys.F
				GLFW_KEY_G -> Keys.G
				GLFW_KEY_H -> Keys.H
				GLFW_KEY_I -> Keys.I
				GLFW_KEY_J -> Keys.J
				GLFW_KEY_K -> Keys.K
				GLFW_KEY_L -> Keys.L
				GLFW_KEY_M -> Keys.M
				GLFW_KEY_N -> Keys.N
				GLFW_KEY_O -> Keys.O
				GLFW_KEY_P -> Keys.P
				GLFW_KEY_Q -> Keys.Q
				GLFW_KEY_R -> Keys.R
				GLFW_KEY_S -> Keys.S
				GLFW_KEY_T -> Keys.T
				GLFW_KEY_U -> Keys.U
				GLFW_KEY_V -> Keys.V
				GLFW_KEY_W -> Keys.W
				GLFW_KEY_X -> Keys.X
				GLFW_KEY_Y -> Keys.Y
				GLFW_KEY_Z -> Keys.Z
				GLFW_KEY_LEFT_BRACKET -> Keys.LEFT_BRACKET
				GLFW_KEY_BACKSLASH -> Keys.BACKSLASH
				GLFW_KEY_RIGHT_BRACKET -> Keys.RIGHT_BRACKET
				GLFW_KEY_ESCAPE -> Keys.ESCAPE
				GLFW_KEY_ENTER -> Keys.ENTER
				GLFW_KEY_TAB -> Keys.TAB
				GLFW_KEY_BACKSPACE -> Keys.BACKSPACE
				GLFW_KEY_INSERT -> Keys.INSERT
				GLFW_KEY_DELETE -> Keys.DELETE
				GLFW_KEY_RIGHT -> Keys.RIGHT
				GLFW_KEY_LEFT -> Keys.LEFT
				GLFW_KEY_DOWN -> Keys.DOWN
				GLFW_KEY_UP -> Keys.UP
				GLFW_KEY_PAGE_UP -> Keys.PAGE_UP
				GLFW_KEY_PAGE_DOWN -> Keys.PAGE_DOWN
				GLFW_KEY_HOME -> Keys.HOME
				GLFW_KEY_END -> Keys.END
				GLFW_KEY_PAUSE -> Keys.PAUSE
				GLFW_KEY_F1 -> Keys.F1
				GLFW_KEY_F2 -> Keys.F2
				GLFW_KEY_F3 -> Keys.F3
				GLFW_KEY_F4 -> Keys.F4
				GLFW_KEY_F5 -> Keys.F5
				GLFW_KEY_F6 -> Keys.F6
				GLFW_KEY_F7 -> Keys.F7
				GLFW_KEY_F8 -> Keys.F8
				GLFW_KEY_F9 -> Keys.F9
				GLFW_KEY_F10 -> Keys.F10
				GLFW_KEY_F11 -> Keys.F11
				GLFW_KEY_F12 -> Keys.F12
				GLFW_KEY_RIGHT_SHIFT, GLFW_KEY_LEFT_SHIFT -> Keys.SHIFT
				GLFW_KEY_RIGHT_CONTROL, GLFW_KEY_LEFT_CONTROL -> Keys.CONTROL
				GLFW_KEY_RIGHT_ALT, GLFW_KEY_LEFT_ALT -> Keys.ALT
				GLFW_KEY_RIGHT_SUPER, GLFW_KEY_LEFT_SUPER -> Keys.SUPER
				GLFW_KEY_MENU -> Keys.MENU
				else -> null
			}
		}
	}
}