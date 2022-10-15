package xyz.cssxsh.novelai

import xyz.cssxsh.novelai.ai.*

public interface NovelAiClientConfig {
    public val proxy: String
    public val doh: String
    public val ipv6: Boolean
    public val timeout: Long
    public val image: ImageModel
    public var token: String
    public val baseUrl: String get() = "https://api.novelai.net"
    public val ban: String get() = "lowres, bad anatomy, bad hands, text, error, missing fingers, extra digit, fewer digits, cropped, worst quality, low quality, normal quality, jpeg artifacts, signature, watermark, username, blurry"
}