package xyz.cssxsh.novelai.ai

import kotlinx.serialization.*

@Serializable
public data class AiRequestImageGenerationTags(
    @SerialName("tags")
    val tags: List<Tag> = emptyList()
) {
    @Serializable
    public data class Tag(
        @SerialName("confidence")
        val confidence: Int = 0,
        @SerialName("count")
        val count: Int = 0,
        @SerialName("tag")
        val tag: String = ""
    )
}