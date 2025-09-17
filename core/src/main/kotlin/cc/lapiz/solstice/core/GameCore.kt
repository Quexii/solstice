package cc.lapiz.solstice.core

import cc.lapiz.solstice.core.event.*
import cc.lapiz.solstice.core.rendering.*
import org.slf4j.*

object GameCore {
	val LOGGER = LoggerFactory.getLogger("GLOBAL")
	val EventQueue = EventQueue()

	fun start() {
		LOGGER.info("Starting solstice Core...")
		RenderSystem.init()
	}

	fun stop() {
		LOGGER.info("Stopping solstice Core...")
	}
}