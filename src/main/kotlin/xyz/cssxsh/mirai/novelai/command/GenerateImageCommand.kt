package xyz.cssxsh.mirai.novelai.command

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.json.*
import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.Image.Key.queryUrl
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
        for (translation in NovelAiHelperConfig.database.data) {
            for ((name, tag) in translation.data) {
                if (tag.name.startsWith(word)) return name
            }
        }
        return null
    }

    @Handler
    public suspend fun CommandSenderOnMessage<*>.handle(vararg tags: String) {
        this as UserCommandSender
        val input: MutableSet<String> = HashSet()
        val params: MutableMap<String, String> = HashMap()
        for (tag in tags) {
            when {
                tag.startsWith("#") -> {
                    try {
                        val (_, key, value) = tag.split('#', '=')
                        params[key] = value
                    } catch (_: Exception) {
                        //
                    }
                }
                tag.startsWith("[") -> continue
                else -> {
                    input.add(translate(word = tag) ?: tag)
                }
            }
        }
        fromEvent.message.findIsInstance<Image>()?.let { source ->
            try {
                val url = source.queryUrl()
                val response = NovelAiHelper.client.http.get(url)
                val packet = response.body<ByteReadPacket>()
//                params["width"] = source.width.toString()
//                params["height"] = source.height.toString()
                params["image"] = packet.encodeBase64()
            } catch (cause: Exception) {
                NovelAiHelper.logger.warning("download image fail", cause)
                null
            }
        }

        NovelAiHelper.logger.info(input.joinToString(" - ", "tags: "))
        val generate = NovelAiHelper.client.ai.generateImage(input = input.joinToString(",")) {
            params.forEach { (key, value) ->
                when {
                    key == "image" -> put(key, value)
                    value.toDoubleOrNull() != null -> put(key, value.toDouble())
                    value.toBooleanStrictOrNull() != null -> put(key, value.toBoolean())
                    else -> put(key, value)
                }
            }
        }
        val image = subject.uploadImage(generate.data.toExternalResource().toAutoCloseable())
        sendMessage(fromEvent.message.quote() + image)
    }
}