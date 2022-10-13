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
        version = "1.0.5",
    ) {
        author("cssxsh")
    }
) {
    public val client: NovelAiClient = NovelAiClient(config = NovelAiHelperConfig)

    override fun onEnable() {
        NovelAiHelperConfig.reload()
        GenerateImageCommand.register()
        LoginCommand.register()
    }

    override fun onDisable() {
        GenerateImageCommand.unregister()
        LoginCommand.unregister()
    }
}