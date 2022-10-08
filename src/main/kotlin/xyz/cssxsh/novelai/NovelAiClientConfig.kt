package xyz.cssxsh.novelai

import xyz.cssxsh.novelai.ai.*

public interface NovelAiClientConfig {
    public val proxy: String
    public val doh: String
    public val ipv6: Boolean
    public val timeout: Long
    public val image: ImageModel
    public var token: String
}