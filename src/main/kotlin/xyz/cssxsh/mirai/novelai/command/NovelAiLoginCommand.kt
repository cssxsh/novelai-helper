package xyz.cssxsh.mirai.novelai.command

import net.mamoe.mirai.console.command.*
import xyz.cssxsh.mirai.novelai.*
import xyz.cssxsh.novelai.*

public object NovelAiLoginCommand : SimpleCommand(
    owner = NovelAiHelper,
    "nai-login",
    description = "登录账号"
) {

    @Handler
    public suspend fun CommandSender.handle(email: String, password: String) {
        val message = try {
            val token = NovelAiHelper.client.user.login(email = email, password = password)
            NovelAiHelper.logger.info("token: $token")
            "登录成功"
        } catch (cause: NovelAiApiException) {
            cause.error.message
        }

        sendMessage(message)
    }
}