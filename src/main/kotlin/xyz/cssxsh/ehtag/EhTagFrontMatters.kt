package xyz.cssxsh.ehtag

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
public data class EhTagFrontMatters(
    @SerialName("abbr")
    val abbr: String = "",
    @SerialName("aliases")
    val aliases: List<String> = emptyList(),
    @SerialName("copyright")
    val copyright: String = "",
    @SerialName("description")
    val description: String,
    @SerialName("example")
    val example: JsonObject? = null,
    @SerialName("key")
    val key: String,
    @SerialName("name")
    val name: String,
    @SerialName("rules")
    val rules: List<String>
)