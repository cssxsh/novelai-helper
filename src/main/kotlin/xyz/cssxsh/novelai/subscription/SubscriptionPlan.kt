package xyz.cssxsh.novelai.subscription

import kotlinx.serialization.*

@Serializable
public enum class SubscriptionPlan {
    NONE, TABLET, SCROLL, OPUS
}