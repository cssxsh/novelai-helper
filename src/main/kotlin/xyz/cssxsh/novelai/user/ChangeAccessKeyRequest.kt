package xyz.cssxsh.novelai.user

import kotlinx.serialization.*

@Serializable
internal data class ChangeAccessKeyRequest(
    @SerialName("currentAccessKey")
    val currentAccessKey: String,
    @SerialName("newAccessKey")
    val newAccessKey: String,
    @SerialName("newEmail")
    val newEmail: String
)