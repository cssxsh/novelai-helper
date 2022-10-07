package xyz.cssxsh.novelai.user

import kotlinx.serialization.*

@Serializable
internal data class LoginRequest(
    @SerialName("key")
    val key: String
)