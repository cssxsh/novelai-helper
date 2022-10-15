package xyz.cssxsh.novelai.subscription

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import xyz.cssxsh.novelai.*

public class SubscriptionController(private val client: NovelAiClient) {
    public suspend fun bind(id: String, processor: PaymentProcessor) {
        val body = BindSubscriptionRequest(paymentProcessor = processor, subscriptionId = id)
        val response = client.http.post("https://api.novelai.net/user/subscription/bind") {
            setBody(body)
            contentType(ContentType.Application.Json)
        }
        response.body<ByteArray>()
    }

    public suspend fun change(plan: SubscriptionPlan) {
        val body = ChangeSubscriptionPlanRequest(newSubscriptionPlan = plan)
        val response = client.http.post("https://api.novelai.net/user/subscription/change") {
            setBody(body)
            contentType(ContentType.Application.Json)
        }
        response.body<ByteArray>()
    }
}