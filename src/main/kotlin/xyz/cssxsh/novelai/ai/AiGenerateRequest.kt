package xyz.cssxsh.novelai.ai

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
internal data class AiGenerateRequest(
    @SerialName("input")
    val input: String,
    @SerialName("model")
    val model: String,
    @SerialName("parameters")
    val parameters: JsonObject
)