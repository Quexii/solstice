package cc.lapiz.solstice.core.assets.meta

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.nio.file.Path
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MetaTexture(path: Path) : Meta<MetaTexture.Serialized>(path) {
    override val version: String
        get() = "0.1"
    override val metaType: String
        get() = "texture"

    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    @OptIn(ExperimentalUuidApi::class)
    private val default = Serialized(
        Serialized.TextureType.Single,
        Serialized.TextureFilter.Linear,
        Serialized.TextureWrap.Clamp,
        version,
        Uuid.random().toString(),
        path.toString()
    ).also { it.metaType = metaType }

    override fun onCreate(): String {
        return json.encodeToString(
            default
        ).also { content = default }
    }

    override fun onModify(original: String, force: Boolean): String {
        val currentSchema = json.encodeToJsonElement(
            default
        ).jsonObject

        val originalObj = try {
            json.parseToJsonElement(original).jsonObject
        } catch (e: Exception) {
            return onCreate()
        }

        val oldVersion = originalObj["version"]?.jsonPrimitive?.contentOrNull
        if (oldVersion == version && !force) return original.also { content = json.decodeFromJsonElement(originalObj) }

        val merged = buildJsonObject {
            for ((key, value) in currentSchema) {
                if (key == "version") {
                    put("version", JsonPrimitive(version))
                } else if (key == "metaType") {
                    put("metaType", JsonPrimitive(metaType))
                } else {
                    put(key, originalObj[key] ?: value)
                }
            }
        }

        content = json.decodeFromJsonElement(merged)

        return json.encodeToString(JsonObject.serializer(), merged)
    }

    override fun loadFromDisk(serialized: String) {
        content = runCatching { json.decodeFromString(Serialized.serializer(), serialized) }
            .getOrNull()
    }

    @Serializable
    data class Serialized(
        var type: TextureType,
        var filter: TextureFilter,
        var wrap: TextureWrap
    ) : BaseSerialized() {
        constructor(
            type: TextureType,
            filter: TextureFilter,
            wrap: TextureWrap,
            version: String,
            uid: String,
            path: String
        ): this(type, filter, wrap) {
            this.version = version
            this.uid = uid
            this.path = path
        }

        @Serializable
        enum class TextureType { Single, Multi, NineSlice }

        @Serializable
        enum class TextureFilter { Linear, Nearest }

        @Serializable
        enum class TextureWrap { Clamp, Repeat }
    }
}
