package cc.lapiz.solstice.serialization

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShaderJSON(
    @SerialName("attributes") val attributes: List<Attribute>,
    @SerialName("name") val name: String,
    @SerialName("shaders") val shaders: List<Shader>,
    @SerialName("uniforms") val uniforms: List<Uniform>
) {
    @Serializable
    data class Attribute(
        @SerialName("name") val name: String,
        @SerialName("type") val type: String
    )

    @Serializable
    data class Shader(
        @SerialName("file") val `file`: String,
        @SerialName("type") val type: String
    )

    @Serializable
    data class Uniform(
        @SerialName("name") val name: String,
        @SerialName("type") val type: String
    )
}