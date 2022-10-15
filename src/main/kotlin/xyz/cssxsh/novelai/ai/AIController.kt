package xyz.cssxsh.novelai.ai

import io.ktor.client.call.*
import io.ktor.client.network.sockets.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import xyz.cssxsh.novelai.*
import kotlin.coroutines.*

public class AIController(private val client: NovelAiClient) {
    public suspend fun generate(input: String, model: String, block: JsonObjectBuilder.() -> Unit): AiGenerate {
        val body = AiGenerateRequest(
            input = input,
            model = model,
            parameters = buildJsonObject(block)
        )
        val response = client.http.post("/ai/generate") {
            setBody(body)
            contentType(ContentType.Application.Json)
        }
        return response.body()
    }

    public suspend fun generateStream(input: String, model: String, block: JsonObjectBuilder.() -> Unit): AiGenerate {
        val body = AiGenerateRequest(
            input = input,
            model = model,
            parameters = buildJsonObject(block)
        )
        val response = client.http.post("/ai/generate-stream") {
            setBody(body)
            contentType(ContentType.Application.Json)
        }
        return response.body()
    }

    /**
     * @param input tags
     * @param model stable-diffusion, nai-diffusion, safe-diffusion, nai-diffusion-furry
     */
    public suspend fun generateImage(
        input: String,
        model: ImageModel = client.config.image,
        block: JsonObjectBuilder.() -> Unit = {}
    ): AiGenerateImage {
        val body = AiGenerateRequest(
            input = input,
            model = Json.encodeToString(model).removeSurrounding("\""),
            parameters = buildJsonObject {
                put("height", 768)
                put("width", 512)
                put("scale", 12)
                put("n_samples", 1)
                put("noise", 0.2)
                put("sampler", "k_euler_ancestral")
                put("seed", System.currentTimeMillis() / 1000)
                put("steps", 28)
                put("strength", 0.7)
                put("uc", client.config.ban)
                put("ucPreset", 0)

                block.invoke(this)
            }
        )
        val statement = client.http.preparePost("/ai/generate-image") {
            setBody(body)
            contentType(ContentType.Application.Json)
        }
        val packet = with(statement) {
            var cause: Exception? = null
            while (coroutineContext.isActive) {
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
        var key = ""
        while (packet.canRead()) {
            key = packet.readUTF8UntilDelimiter(":")
            packet.readByte()
            when (key.trim()) {
                "event" -> event = packet.readUTF8UntilDelimiter("\n").trim()
                "id" -> id = packet.readUTF8UntilDelimiter("\n").trim().toLong()
                "data" -> data = packet.decodeBase64Bytes().readBytes()
            }
        }
        if (data.isEmpty()) {
            throw NovelAiApiException(error = Json.decodeFromString(key))
        }

        return AiGenerateImage(event, id, data)
    }

    public suspend fun classify(): AiSequenceClassification {
        val response = client.http.post("/ai/classify") {
            contentType(ContentType.Application.Json)
        }
        return response.body()
    }

    public suspend fun generateImageTags(model: String, prompt: String): AiRequestImageGenerationTags {
        val response = client.http.get("/ai/generate-image/suggest-tags") {
            parameter("model", model)
            parameter("prompt", prompt)
        }
        return response.body()
    }

    public suspend fun generateVoice(text: String, seed: String): AiSequenceClassification {
        val response = client.http.get("/ai/generate-voice") {
            parameter("text", text)
            parameter("seed", seed)
            parameter("voice", "")
            parameter("opus", false)
            parameter("version", "")
        }
        return response.body()
    }
}