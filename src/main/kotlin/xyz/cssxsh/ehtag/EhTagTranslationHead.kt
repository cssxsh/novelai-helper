package xyz.cssxsh.ehtag

import kotlinx.serialization.*

@Serializable
public data class EhTagTranslationHead(
    @SerialName("author")
    val author: EhTagAuthor,
    @SerialName("committer")
    val committer: EhTagAuthor,
    @SerialName("message")
    val message: String,
    @SerialName("sha")
    val sha: String
)