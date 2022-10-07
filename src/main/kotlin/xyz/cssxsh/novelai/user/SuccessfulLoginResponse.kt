package xyz.cssxsh.novelai.user

import kotlinx.serialization.*

@Serializable
internal data class SuccessfulLoginResponse(
    @SerialName("accessToken")
    val accessToken: String
)