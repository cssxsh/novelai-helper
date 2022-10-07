package xyz.cssxsh.novelai.user

import kotlinx.serialization.*

@Serializable
public data class AccountInformation(
    @SerialName("accountCreatedAt")
    val accountCreatedAt: Long = 0,
    @SerialName("emailVerificationLetterSent")
    val emailVerificationLetterSent: Boolean = false,
    @SerialName("emailVerified")
    val emailVerified: Boolean = false,
    @SerialName("trialActionsLeft")
    val trialActionsLeft: Long = 0,
    @SerialName("trialActivated")
    val trialActivated: Boolean = false
)