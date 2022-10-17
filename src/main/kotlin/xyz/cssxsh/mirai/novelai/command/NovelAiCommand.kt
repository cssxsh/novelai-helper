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
import xyz.cssxsh.novelai.*
import kotlin.random.*

public object NovelAiCommand : SimpleCommand(
    owner = NovelAiHelper,
    "nai",
    description = "生成图片"
) {

    private val random = Random(seed = System.currentTimeMillis())

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
                    input.add(NovelAiHelper.translate(word = tag) ?: tag)
                }
            }
        }
        fromEvent.message.findIsInstance<Image>()?.let { source ->
            if (NovelAiHelperConfig.image2image.not()) return
            try {
                val url = source.queryUrl()
                val response = NovelAiHelper.client.http.get(url)
                val packet = response.body<ByteReadPacket>()
                params["image"] = packet.encodeBase64()
            } catch (cause: Exception) {
                NovelAiHelper.logger.warning("download image fail", cause)
                null
            }
        }

        val seed = random.nextLong(0, 2L shl 32 - 1)
        NovelAiHelper.logger.info(input.joinToString(", ", "generate image seed: $seed, tags: "))
        val generate = try {
            NovelAiHelper.client.ai.generateImage(input = input.joinToString(",")) {
                put("seed", seed)
                params.forEach { (key, value) ->
                    when {
                        key == "image" -> put(key, value)
                        value.toDoubleOrNull() != null -> put(key, value.toDouble())
                        value.toBooleanStrictOrNull() != null -> put(key, value.toBoolean())
                        else -> put(key, value)
                    }
                }
            }
        } catch (cause: NovelAiApiException) {
            sendMessage(cause.error.message)
            return
        }
        val image = generate.data.toExternalResource().use { subject.uploadImage(it) }
        sendMessage(fromEvent.message.quote() + image + "\nseed=$seed")
    }
}