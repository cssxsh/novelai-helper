package xyz.cssxsh.mirai.novelai.command

import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import xyz.cssxsh.mirai.novelai.*
import xyz.cssxsh.mirai.novelai.data.*

public object GenerateImageCommand : SimpleCommand(
    owner = NovelAiHelper,
    "nai",
    description = "生成图片"
)  {

    private fun translate(word: String): String? {
        for (translation in NovelAiHelperConfig.database.data) {
            for ((name, tag) in translation.data) {
                if (tag.name == word) return name
            }
        }
        return null
    }

    @Handler
    public suspend fun CommandSenderOnMessage<*>.handle(vararg tags: String) {
        this as UserCommandSender
        val input: MutableSet<String> = HashSet()
        for (tag in tags) {
            input.add(translate(word = tag) ?: tag)
        }
        NovelAiHelper.logger.info(input.joinToString(" - ", "tags: "))
        val generate = NovelAiHelper.client.ai.generateImage(input = input.joinToString(",")) {
            //
        }
        val image = subject.uploadImage(generate.data.toExternalResource().toAutoCloseable())
        sendMessage(fromEvent.message.quote() + image)
    }
}