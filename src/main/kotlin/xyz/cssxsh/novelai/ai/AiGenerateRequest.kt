package xyz.cssxsh.novelai.ai

import kotlinx.serialization.*

@Serializable
public data class AiGenerateRequest(
    @SerialName("input")
    val input: String = "",
    @SerialName("model")
    val model: String = "",
    @SerialName("parameters")
    val parameters: Parameters = Parameters()
) {
    @Serializable
    public data class Parameters(
        @SerialName("max_length")
        val maxLength: Int = 0,
        @SerialName("min_length")
        val minLength: Int = 0,
        @SerialName("temperature")
        val temperature: Int = 0,
        @SerialName("use_string")
        val useString: Boolean = false
    )
}