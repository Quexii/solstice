package cc.lapiz.solstice.resource

interface Loadable {
	val path: String
	fun load()
}