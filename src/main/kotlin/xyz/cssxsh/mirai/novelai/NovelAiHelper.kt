package xyz.cssxsh.mirai.novelai

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.*
import xyz.cssxsh.mirai.novelai.command.*
import xyz.cssxsh.mirai.novelai.data.*
import xyz.cssxsh.novelai.*

public object NovelAiHelper : KotlinPlugin(
    JvmPluginDescription(
        id = "xyz.cssxsh.mirai.plugin.novelai-helper",
        name = "novelai-helper",
        version = "1.0.7",
    ) {
        author("cssxsh")
    }
) {
    public val client: NovelAiClient = NovelAiClient(config = NovelAiHelperConfig, default = true)

    public fun translate(word: String): String? {
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

    override fun onEnable() {
        NovelAiHelperConfig.reload()
        NovelAiHelperConfig.save()
        NovelAiCommand.register()
        NovelAiLoginCommand.register()
    }

    override fun onDisable() {
        NovelAiCommand.unregister()
        NovelAiLoginCommand.unregister()
    }
}