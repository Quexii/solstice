package cc.lapiz.solstice.core.assets.meta

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.nio.file.Path
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

typealias MetaTextureType = MetaTexture.Serialized.TextureType
typealias MetaTextureFilter = MetaTexture.Serialized.TextureFilter
typealias MetaTextureWrap = MetaTexture.Serialized.TextureWrap
class MetaTexture(path: Path) : Meta<MetaTexture.Serialized>(path) {
    override val version: String
        get() = "0.1"

    @OptIn(ExperimentalUuidApi::class)
    private val default = Serialized(
        Serialized.TextureType.Single,
        Serialized.TextureFilter.Linear,
        Serialized.TextureWrap.Clamp,
        version,
        Uuid.random().toString(),
        path.toString()
    )

    override fun onCreate(): String {
        return Json.encodeToString(
            default
        ).also { content = default }
    }

    override fun onModify(original: String, force: Boolean): String {
        val json = Json { ignoreUnknownKeys = true }

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
                } else {
                    put(key, originalObj[key] ?: value)
                }
            }
        }

        content = json.decodeFromJsonElement(merged)

        return json.encodeToString(merged)
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
