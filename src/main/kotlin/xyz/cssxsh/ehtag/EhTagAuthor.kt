package xyz.cssxsh.ehtag

import kotlinx.serialization.*

@Serializable
public data class EhTagAuthor(
    @SerialName("email")
    val email: String,
    @SerialName("name")
    val name: String,
    @SerialName("when")
    val `when`: String
)