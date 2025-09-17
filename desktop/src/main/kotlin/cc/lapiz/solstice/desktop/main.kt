package cc.lapiz.solstice.desktop

import cc.lapiz.solstice.core.GameCore

fun main() {
	GameCore.LOGGER.info("Starting GameCore")
	val desktop = Desktop()
	desktop.start()
}