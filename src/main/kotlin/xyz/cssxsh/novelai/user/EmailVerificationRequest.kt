package xyz.cssxsh.novelai.user

import kotlinx.serialization.*

@Serializable
internal data class EmailVerificationRequest(
    @SerialName("verificationToken")
    val verificationToken: String
)