package xyz.cssxsh.novelai.ai

import kotlinx.serialization.*

@Serializable
public data class AiGenerate(
    @SerialName("error")
    val error: String = "",
    @SerialName("output")
    val output: String = ""
)