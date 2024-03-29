package xyz.cssxsh.novelai

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*
import okhttp3.Dns
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.dnsoverhttps.DnsOverHttps
import xyz.cssxsh.novelai.ai.*
import xyz.cssxsh.novelai.subscription.*
import xyz.cssxsh.novelai.user.*
import java.net.*

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
                if (response.status.value in 400..499) throw ClientRequestException(response, response.body())
                if (response.status.value in 500..599) throw ServerResponseException(response, response.body())
            }
        }
        Auth {
            bearer {
                loadTokens {
                    BearerTokens(config.token, "")
                }
                refreshTokens {
                    BearerTokens(config.token, "")
                }
                sendWithoutRequest { builder ->
                    when (builder.url.encodedPathSegments.lastOrNull()) {
                        "register" -> false
                        "login" -> false
                        "resend-email-verification" -> false
                        "verify-email" -> false
                        else -> true
                    }
                }
            }
        }
        BrowserUserAgent()
        ContentEncoding()
        engine {
            config {
                dns(object : Dns {
                    private val doh = DnsOverHttps.Builder()
                        .client(okhttp3.OkHttpClient())
                        .url(config.doh.toHttpUrl())
                        .includeIPv6(config.ipv6)
                        .build()

                    @Throws(UnknownHostException::class)
                    override fun lookup(hostname: String): List<InetAddress> {
                        return try {
                            doh.lookup(hostname)
                        } catch (_: UnknownHostException) {
                            Dns.SYSTEM.lookup(hostname)
                        }
                    }
                })
                proxy(config.proxy.ifEmpty { null }?.let { urlString ->
                    val url = Url(urlString)
                    when (url.protocol) {
                        URLProtocol.HTTP -> Proxy(Proxy.Type.HTTP, InetSocketAddress(url.host, url.port))
                        URLProtocol.SOCKS -> Proxy(Proxy.Type.SOCKS, InetSocketAddress(url.host, url.port))
                        else -> throw IllegalArgumentException("Illegal Proxy: $urlString")
                    }
                })
            }
        }
    }
    public open val ai: AIController by lazy { AIController(this) }
    public open val subscription: SubscriptionController by lazy { SubscriptionController(this) }
    public open val user: UserController by lazy { UserController(this) }
    public open val module: AIModuleController by lazy { AIModuleController(this) }
}