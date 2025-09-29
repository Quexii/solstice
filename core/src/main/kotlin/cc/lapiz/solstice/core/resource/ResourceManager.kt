package cc.lapiz.solstice.core.resource

object ResourceManager {
	private val resources = mutableMapOf<String, Resource>()

	fun register(resource: Resource) {
		resources[resource.id] = resource
	}

	fun <T: Resource> get(id: String): T? = resources[id] as T?

	fun <T> load(resource: T): Resource where T : Resource, T : Loadable {
		register(resource)
		resource.load()
		return resource
	}
}