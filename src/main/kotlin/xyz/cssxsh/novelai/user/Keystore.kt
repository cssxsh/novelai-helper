package xyz.cssxsh.novelai.user

import kotlinx.serialization.*

@Serializable
public data class Keystore(
    @SerialName("keystore")
    val keystore: String? = null,
    @SerialName("changeIndex")
    val changeIndex: Int = 0
)