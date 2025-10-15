package cc.lapiz.solstice.serialization


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FontJSON(
    @SerialName("fonts") val fonts: List<Font>,
    @SerialName("license") val license: String,
    @SerialName("name") val name: String,
    @SerialName("path") val path: String,
	@SerialName("resolution") val resolution: Int,
	@SerialName("size") val size: Int,
	@SerialName("pixelFont") val pixelFont: Boolean,
) {
    @Serializable
    data class Font(
	    @SerialName("default") val default: Boolean? = null,
        @SerialName("face") val face: String,
        @SerialName("file") val file: String
    )
}