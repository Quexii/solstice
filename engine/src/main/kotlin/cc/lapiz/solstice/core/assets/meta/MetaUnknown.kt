package cc.lapiz.solstice.core.assets.meta

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Path
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MetaUnknown(path: Path) : Meta<Meta.BaseSerialized>(path) {
    override val version: String
        get() = "0.1"
    override val metaType: String
        get() = "unknown"

    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun default(): BaseSerialized = BaseSerialized(
        version = version,
        uid = Uuid.random().toString(),
        path = path.toString(),
        metaType = metaType
    )

    override fun onCreate(): String {
        val defaults = default()
        content = defaults
        return json.encodeToString(BaseSerialized.serializer(), defaults)
    }

    override fun onModify(original: String, force: Boolean): String {
        val defaults = default()
        val parsed = runCatching { json.decodeFromString(BaseSerialized.serializer(), original) }
            .getOrElse { defaults }

        parsed.version = version
        parsed.metaType = metaType
        parsed.path = path.toString()
        if (parsed.uid.isBlank()) parsed.uid = defaults.uid

        content = parsed
        return json.encodeToString(BaseSerialized.serializer(), parsed)
    }

    override fun loadFromDisk(serialized: String) {
        content = runCatching { json.decodeFromString(BaseSerialized.serializer(), serialized) }.getOrNull()
    }
}