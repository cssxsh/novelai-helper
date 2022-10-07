package xyz.cssxsh.mirai.novelai.data

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.*
import net.mamoe.mirai.console.data.*
import net.mamoe.mirai.console.plugin.jvm.*
import net.mamoe.mirai.console.util.*
import xyz.cssxsh.ehtag.*
import xyz.cssxsh.novelai.*
import xyz.cssxsh.novelai.ai.*
import java.io.File

public object NovelAiHelperConfig : ReadOnlyPluginConfig("config"), NovelAiClientConfig {

    override val serializersModule: SerializersModule = SerializersModule {
        contextual(ImageModel.serializer())
    }

    override val doh: String by value("https://public.dns.iij.jp/dns-query")
    override val ipv6: Boolean by value(true)
    override val timeout: Long by value(30_000L)
    override val image: ImageModel by value(ImageModel.SAFE_DIFFUSION)

    override var token: String = ""
        get() = field.ifEmpty { token0.readText() }
        set(value) {
            token0.writeText(value)
            field = value
        }

    public var database: EhTagTranslationDatabase = EhTagTranslationDatabase.Empty
        get() {
            return if (field.data.isEmpty() && database0.exists()) {
                val database = Json.decodeFromString(EhTagTranslationDatabase.serializer(), database0.readText())
                field = database
                database
            } else {
                field
            }
        }
        set(value) {
            database0.writeText(Json.encodeToString(EhTagTranslationDatabase.serializer(), value))
            field = value
        }

    private var token0 = File("./token.txt")

    private var database0 = File("./db.text.json")

    @ConsoleExperimentalApi
    override fun onInit(owner: PluginDataHolder, storage: PluginDataStorage) {
        if (owner is JvmPlugin) {
            token0 = owner.resolveDataFile("./token.txt")
            if (!token0.exists()) token0.createNewFile()
            database0 = owner.resolveDataFile("./db.text.json")
            owner.launch {
                val http = HttpClient(OkHttp) {
                    BrowserUserAgent()
                    ContentEncoding()
                }
                val statement = http.prepareGet("https://github.com/EhTagTranslation/Database/releases/latest/download/db.text.json")
                while (isActive) {
                    try {
                        database0.writeBytes(statement.body())
                        break
                    } catch (_: SocketTimeoutException) {
                        continue
                    } catch (_: ConnectTimeoutException) {
                        continue
                    }
                }
            }
        }
    }
}