package xyz.cssxsh.ehtag

import kotlinx.serialization.*

@Serializable
public data class EhTagTranslationDatabase(
    @SerialName("data")
    val `data`: List<EhTagTranslation>,
    @SerialName("head")
    val head: EhTagTranslationHead?,
    @SerialName("repo")
    val repo: String,
    @SerialName("version")
    val version: Int
) {
    public companion object {
        public val Empty: EhTagTranslationDatabase = EhTagTranslationDatabase(
            data = emptyList(),
            head = null,
            repo = "",
            version = 0
        )
    }
}
