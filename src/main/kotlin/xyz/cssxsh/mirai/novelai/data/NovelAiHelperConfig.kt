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

    override val proxy: String by value("")
    override val doh: String by value("https://public.dns.iij.jp/dns-query")
    override val ipv6: Boolean by value(true)
    override val timeout: Long by value(30_000L)
    override val image: ImageModel by value(ImageModel.SAFE_DIFFUSION)

    override var token: String = ""
        get() {
            return field.ifEmpty {
                val value = token0.readText()
                field = value
                value
            }
        }
        set(value) {
            token0.writeText(value)
            field = value
        }

    override var ban: String = ""

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

    private var ban0 = File("./ban.txt")

    private var database0 = File("./db.text.json")

    @ConsoleExperimentalApi
    override fun onInit(owner: PluginDataHolder, storage: PluginDataStorage) {
        if (owner !is JvmPlugin) return
        token0 = owner.resolveDataFile("./token.txt")
        if (!token0.exists()) token0.createNewFile()
        ban0 = owner.resolveConfigFile("./ban.txt")
        if (!ban0.exists()) ban0.writeText(super.ban)
        database0 = owner.resolveDataFile("./db.text.json")
        owner.launch {
            val http = HttpClient(OkHttp) {
                BrowserUserAgent()
                ContentEncoding()
            }
            val statement =
                http.prepareGet("https://github.com/EhTagTranslation/Database/releases/latest/download/db.text.json")
            while (isActive) {
                try {
                    database0.writeBytes(statement.body())
                    break
                } catch (_: SocketTimeoutException) {
                    continue
                } catch (_: ConnectTimeoutException) {
                    continue
                } catch (_: java.net.SocketException) {
                    owner.logger.warning("翻译词典下载失败，正在尝试重新下载")
                    continue
                }
            }
        }
        owner.launch {
            ban = ban0.readText()
            var last = ban0.lastModified()
            while (isActive) {
                delay(60_000)
                try {
                    val timestamp = ban0.lastModified()
                    if (last < timestamp) {
                        last = timestamp
                        ban = ban0.readText()
                        owner.logger.info("BAN 词条已更新")
                    }
                } catch (_: Exception) {
                    owner.logger.warning("BAN 词条检查失败")
                    continue
                }
            }
        }
    }
}