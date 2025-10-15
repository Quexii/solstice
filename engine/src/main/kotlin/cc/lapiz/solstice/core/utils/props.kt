package cc.lapiz.solstice.utils

object Props {
	const val VERSION = "0.0.1-alpha"
	const val APP_NAME = "Solstice"

	var DEBUG = false
	var EDITOR = false

	fun init(args: Array<String>) {
		EDITOR = JSystem.getProperty("solstice.editor") != null && JSystem.getProperty("solstice.editor").equals("true", ignoreCase = true)
	}
}