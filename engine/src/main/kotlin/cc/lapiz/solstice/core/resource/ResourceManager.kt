package cc.lapiz.solstice.core.resource

object ResourceManager {
	private val resources = mutableMapOf<String, Id>()

	fun register(id: Id) {
		resources[id.id] = id
	}

	fun <T: Id> get(id: String): T? = resources[id] as T?

	fun <T> load(resource: T): Id where T : Id, T : Loadable {
		register(resource)
		resource.load()
		return resource
	}

	fun getAll(): Collection<Id> = resources.values
}