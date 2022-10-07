package xyz.cssxsh.novelai.ai

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
public data class AiSequenceClassification(
    @SerialName("error")
    val error: String = "",
    @SerialName("output")
    val output: List<JsonElement> = emptyList()
)