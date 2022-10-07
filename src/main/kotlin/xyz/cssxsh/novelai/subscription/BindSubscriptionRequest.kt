package xyz.cssxsh.novelai.subscription

import kotlinx.serialization.*

@Serializable
internal data class BindSubscriptionRequest(
    @SerialName("paymentProcessor")
    val paymentProcessor: PaymentProcessor,
    @SerialName("subscriptionId")
    val subscriptionId: String
)