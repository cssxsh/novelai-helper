package xyz.cssxsh.novelai

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.dnsoverhttps.DnsOverHttps
import xyz.cssxsh.novelai.ai.*
import xyz.cssxsh.novelai.subscription.*
import xyz.cssxsh.novelai.user.*

public open class NovelAiClient(internal val config: NovelAiClientConfig) {
    public open val http: HttpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(json = Json)
        }
        install(HttpTimeout) {
            socketTimeoutMillis = config.timeout
            connectTimeoutMillis = config.timeout
            requestTimeoutMillis = null
        }
        defaultRequest {
            header(HttpHeaders.Authorization, "Bearer ${config.token}")
        }
        HttpResponseValidator {
            validateResponse { response ->
                when (response.status) {
                    HttpStatusCode.BadRequest -> throw NovelAiApiException(error = response.body())
                    HttpStatusCode.Unauthorized -> throw NovelAiApiException(error = response.body())
                    HttpStatusCode.PaymentRequired -> throw NovelAiApiException(error = response.body())
                    HttpStatusCode.NotFound -> throw NovelAiApiException(error = response.body())
                    HttpStatusCode.Conflict -> throw NovelAiApiException(error = response.body())
                    HttpStatusCode.InternalServerError -> throw NovelAiApiException(error = response.body())
                }
            }
        }
        BrowserUserAgent()
        ContentEncoding()
        engine {
            config {
                dns(
                    DnsOverHttps.Builder()
                        .client(okhttp3.OkHttpClient())
                        .url(config.doh.toHttpUrl())
                        .includeIPv6(config.ipv6)
                        .build()
                )
            }
        }
    }
    public open val ai: AIController by lazy { AIController(this) }
    public open val subscription: SubscriptionController by lazy { SubscriptionController(this) }
    public open val user: UserController by lazy { UserController(this) }
    public open val module: AIModuleController by lazy { AIModuleController(this) }
}