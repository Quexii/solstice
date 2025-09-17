package cc.lapiz.solstice.core.resource

interface Loadable {
	val path: String
	fun load()
}