package xyz.cssxsh.novelai

public class NovelAiApiException(public val error: NovelAiApiError) : IllegalStateException() {
    override val message: String get() = error.message
}