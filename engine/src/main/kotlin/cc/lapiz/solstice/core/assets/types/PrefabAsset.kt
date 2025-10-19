package cc.lapiz.solstice.core.assets.types

import cc.lapiz.solstice.core.assets.Asset
import cc.lapiz.solstice.core.assets.meta.MetaPrefab
import kotlinx.serialization.json.JsonElement

class PrefabAsset(private val prefabMeta: MetaPrefab.Serialized) : Asset(prefabMeta) {
    val name: String get() = prefabMeta.name
    val definition: JsonElement get() = prefabMeta.definition
    val tags: List<String> get() = prefabMeta.tags
    val isTemplate: Boolean get() = prefabMeta.template
}
