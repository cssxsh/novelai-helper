package xyz.cssxsh.novelai

public interface NovelAiClientConfig {
    public val doh: String
    public val ipv6: Boolean
    public val timeout: Long
    public var token: String
}