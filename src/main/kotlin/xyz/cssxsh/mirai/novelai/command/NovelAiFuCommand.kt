package xyz.cssxsh.mirai.novelai.command

import io.ktor.client.call.*
import io.ktor.client.network.sockets.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import xyz.cssxsh.mirai.novelai.*
import xyz.cssxsh.mirai.novelai.data.*
import xyz.cssxsh.novelai.*
import xyz.cssxsh.novelai.ai.*
import kotlin.random.*

public object NovelAiFuCommand : SimpleCommand(
    owner = NovelAiHelper,
    "nai-fu", "naifu",
    description = "生成图片"
) {

    private val random = Random(seed = System.currentTimeMillis())

    private suspend fun colab(prompt: String, block: JsonObjectBuilder.() -> Unit = {}): AiGenerateImage {
        val body = buildJsonObject {
            put("prompt", prompt)
            put("height", 768)
            put("width", 512)
            put("scale", 12)
            put("n_samples", 1)
            put("noise", 0.2)
            put("sampler", "k_euler_ancestral")
            put("steps", 28)
            put("strength", 0.7)
            put("uc", NovelAiHelperConfig.ban)
            put("ucPreset", 0)

            block.invoke(this)
        }
        val statement = NovelAiHelper.client.http.preparePost {
            url {
                takeFrom(NovelAiHelperConfig.local)
                takeFrom("/generate-stream")
            }
            setBody(body)
            contentType(ContentType.Application.Json)
        }
        val packet = with(statement) {
            var cause: Exception? = null
            while (kotlin.coroutines.coroutineContext.isActive) {
                return@with try {
                    body<ByteReadPacket>()
                } catch (exception: SocketTimeoutException) {
                    cause = exception
                    continue
                } catch (exception: ConnectTimeoutException) {
                    cause = exception
                    continue
                }
            }
            throw CancellationException("generate image timeout", cause)
        }
        var event = ""
        var id = 0L
        var data = ByteArray(0)
        while (packet.canRead()) {
            val key = packet.readUTF8UntilDelimiter(":")
            packet.readByte()
            when (key.trim()) {
                "event" -> event = packet.readUTF8UntilDelimiter("\n").trim()
                "id" -> id = packet.readUTF8UntilDelimiter("\n").trim().toLong()
                "data" -> data = packet.decodeBase64Bytes().readBytes()
            }
        }

        return AiGenerateImage(event, id, data)
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
                    input.add(NovelAiHelper.translate(word = tag) ?: tag)
                }
            }
        }
        fromEvent.message.findIsInstance<Image>()?.let { source ->
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
            colab(prompt = input.joinToString(",")) {
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
        val image = subject.uploadImage(generate.data.toExternalResource().toAutoCloseable())
        sendMessage(fromEvent.message.quote() + image + "\nseed=$seed")
    }
}