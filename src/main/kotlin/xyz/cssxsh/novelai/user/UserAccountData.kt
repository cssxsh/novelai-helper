package xyz.cssxsh.novelai.user

import kotlinx.serialization.*

@Serializable
public data class UserAccountData(
    @SerialName("information")
    val information: AccountInformation,
    @SerialName("keystore")
    val keystore: Keystore,
    @SerialName("priority")
    val priority: Priority,
    @SerialName("settings")
    val settings: String = "",
    @SerialName("subscription")
    val subscription: Subscription
)