package xyz.cssxsh.mirai.novelai

import net.mamoe.mirai.console.plugin.jvm.*
import net.mamoe.mirai.utils.info

public object NovelAiHelper : KotlinPlugin(
    JvmPluginDescription(
        id = "xyz.cssxsh.mirai.plugin.novelai-helper",
        name = "novelai-helper",
        version = "1.0.0",
    ) {
        author("cssxsh")
    }
) {
    override fun onEnable() {
        logger.info { "Plugin loaded" }
    }
}