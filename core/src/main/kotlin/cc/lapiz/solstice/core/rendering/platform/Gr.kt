package cc.lapiz.solstice.core.rendering.platform

object Gr {
	lateinit var Functions: Functions
		private set
	lateinit var Types: Types
		private set
	lateinit var Capabilities: Capabilities
		private set

	fun init(functions: Functions, types: Types, capabilities: Capabilities) {
		Functions = functions
		Types = types
		Capabilities = capabilities
	}
}