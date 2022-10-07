package xyz.cssxsh.novelai.ai

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import xyz.cssxsh.novelai.*

public class AIController(private val client: NovelAiClient) {
    public suspend fun generate(input: String): AiGenerate {
        val body = AiGenerateRequest(
            input = input,
            model = "model"
        )
        val response = client.http.post("https://api.novelai.net/ai/generate") {
            setBody(body)
            contentType(ContentType.Application.Json)
        }
        return response.body()
    }

    public suspend fun generateStream(input: String): AiGenerate {
        val body = AiGenerateRequest(
            input = input,
            model = "model"
        )
        val response = client.http.post("https://api.novelai.net/ai/generate-stream") {
            setBody(body)
            contentType(ContentType.Application.Json)
        }
        return response.body()
    }

    public suspend fun classify(input: String): AiSequenceClassification {
        val body = AiGenerateRequest(
            input = input,
            model = "model"
        )
        val response = client.http.post("https://api.novelai.net/ai/classify") {
            setBody(body)
            contentType(ContentType.Application.Json)
        }
        return response.body()
    }

    public suspend fun generateImageTags(model: String, prompt: String): AiRequestImageGenerationTags {
        val response = client.http.get("https://api.novelai.net/ai/generate-image/suggest-tags") {
            parameter("model", model)
            parameter("prompt", prompt)
        }
        return response.body()
    }

    public suspend fun generateVoice(text: String, seed: String): AiSequenceClassification {
        val response = client.http.get("https://api.novelai.net/ai/generate-voice") {
            parameter("text", text)
            parameter("seed", seed)
            parameter("voice", "")
            parameter("opus", false)
            parameter("version", "")
        }
        return response.body()
    }
}