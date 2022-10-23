package xyz.cssxsh.mirai.novelai.command

import net.mamoe.mirai.console.command.*
import xyz.cssxsh.mirai.novelai.*
import xyz.cssxsh.mirai.novelai.data.*

public object NovelAiReLoadCommand : SimpleCommand(
    owner = NovelAiHelper,
    "nai-reload",
    description = "重载配置"
) {

    @Handler
    public suspend fun CommandSender.handle() {
        with(NovelAiHelper) {
            NovelAiHelperConfig.reload()
        }

        sendMessage("配置已重载")
    }
}