package xyz.cssxsh.ehtag

import kotlinx.serialization.*

@Serializable
public data class EhTag(
    @SerialName("intro")
    val intro: String,
    @SerialName("links")
    val links: String,
    @SerialName("name")
    val name: String
)