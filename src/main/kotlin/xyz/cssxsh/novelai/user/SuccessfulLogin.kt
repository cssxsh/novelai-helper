package xyz.cssxsh.novelai.user

import kotlinx.serialization.*

@Serializable
internal data class SuccessfulLogin(
    @SerialName("accessToken")
    val accessToken: String
)