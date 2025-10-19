package cc.lapiz.solstice.core.assets.meta

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.buildJsonObject
import java.nio.file.Path
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MetaPrefab(
    path: Path,
    private val seed: Serialized? = null,
) : Meta<MetaPrefab.Serialized>(path) {
    override val version: String
        get() = "0.1"
    override val metaType: String
        get() = "prefab"

    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun default(): Serialized {
        val base = seed?.copy()?.also {
            it.uid = seed.uid
            it.version = seed.version
            it.path = seed.path
            it.metaType = seed.metaType
        } ?: Serialized(
            name = path.fileName?.toString()?.substringBefore('.') ?: "Prefab",
            definition = buildJsonObject { }
        )

        if (base.uid.isBlank()) base.uid = Uuid.random().toString()
        base.version = version
        base.metaType = metaType
        base.path = path.toString()
        return base
    }

    override fun onCreate(): String {
        val defaults = default()
        content = defaults
        return json.encodeToString(Serialized.serializer(), defaults)
    }

    override fun onModify(original: String, force: Boolean): String {
        val defaults = default()
        val parsed = runCatching { json.decodeFromString(Serialized.serializer(), original) }
            .getOrElse { defaults }

        if (parsed.uid.isBlank()) parsed.uid = defaults.uid
        parsed.version = version
        parsed.metaType = metaType
        parsed.path = path.toString()

        content = parsed
        return json.encodeToString(Serialized.serializer(), parsed)
    }

    override fun loadFromDisk(serialized: String) {
        content = runCatching { json.decodeFromString(Serialized.serializer(), serialized) }.getOrNull()
    }

    @Serializable
    data class Serialized(
        var name: String,
        var definition: JsonElement = JsonNull,
        var tags: List<String> = emptyList(),
        @SerialName("isTemplate")
        var template: Boolean = false,
    ) : BaseSerialized()
}
