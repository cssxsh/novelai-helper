package xyz.cssxsh.ehtag

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
public data class EhTagTranslation(
    @SerialName("count")
    val count: Int,
    @SerialName("data")
    val `data`: Map<String, EhTag>,
    @SerialName("frontMatters")
    val frontMatters: EhTagFrontMatters,
    @SerialName("namespace")
    val namespace: String
)