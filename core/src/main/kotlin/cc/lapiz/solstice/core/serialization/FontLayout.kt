package cc.lapiz.solstice.core.serialization


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FontLayout(
    @SerialName("fonts") val fonts: List<Font>,
    @SerialName("license") val license: String,
    @SerialName("name") val name: String,
    @SerialName("path") val path: String
) {
    @Serializable
    data class Font(
	    @SerialName("default") val default: Boolean? = null,
        @SerialName("face") val face: String,
        @SerialName("file") val file: String
    )
}