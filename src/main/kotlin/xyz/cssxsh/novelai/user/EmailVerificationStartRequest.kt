package xyz.cssxsh.novelai.user

import kotlinx.serialization.*

@Serializable
internal data class EmailVerificationStartRequest(
    @SerialName("email")
    val email: String
)