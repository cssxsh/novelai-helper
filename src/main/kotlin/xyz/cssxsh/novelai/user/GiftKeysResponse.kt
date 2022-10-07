package xyz.cssxsh.novelai.user

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
public data class GiftKeysResponse(
    @SerialName("giftKeys")
    val giftKeys: List<JsonObject>
)