package xyz.cssxsh.novelai

import kotlinx.serialization.*

@Serializable
public data class NovelAiApiError(
    @SerialName("message")
    val message: String,
    @SerialName("statusCode")
    val statusCode: Int
)