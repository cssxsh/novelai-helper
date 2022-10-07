package xyz.cssxsh.novelai.user

import kotlinx.serialization.*

@Serializable
internal data class CreateUserRequest(
    @SerialName("email")
    val email: String,
    @SerialName("emailCleartext")
    val emailCleartext: String,
    @SerialName("giftkey")
    val giftkey: String,
    @SerialName("key")
    val key: String,
    @SerialName("recaptcha")
    val recaptcha: String
)