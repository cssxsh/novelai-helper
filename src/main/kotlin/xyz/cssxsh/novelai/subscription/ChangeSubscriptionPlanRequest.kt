package xyz.cssxsh.novelai.subscription

import kotlinx.serialization.*

@Serializable
internal data class ChangeSubscriptionPlanRequest(
    @SerialName("newSubscriptionPlan")
    val newSubscriptionPlan: SubscriptionPlan
)