package xyz.cssxsh.novelai.user

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
public data class Subscription(
    @SerialName("active")
    val active: Boolean = false,
    @SerialName("expiresAt")
    val expiresAt: Int = 0,
    @SerialName("paymentProcessorData")
    val paymentProcessorData: JsonElement,
    @SerialName("perks")
    val perks: Perks,
    @SerialName("tier")
    val tier: Int = 0,
    @SerialName("trainingStepsLeft")
    val trainingStepsLeft: TrainingStepsLeft
) {

    @Serializable
    public data class Perks(
        @SerialName("contextTokens")
        val contextTokens: Int = 0,
        @SerialName("maxPriorityActions")
        val maxPriorityActions: Int = 0,
        @SerialName("moduleTrainingSteps")
        val moduleTrainingSteps: Int = 0,
        @SerialName("startPriority")
        val startPriority: Int = 0,
        @SerialName("unlimitedMaxPriority")
        val unlimitedMaxPriority: Boolean = false,
        @SerialName("voiceGeneration")
        val voiceGeneration: Boolean = false,
        @SerialName("imageGeneration")
        val imageGeneration: Boolean = false,
        @SerialName("unlimitedImageGeneration")
        val unlimitedImageGeneration: Boolean = false,
        @SerialName("unlimitedImageGenerationLimits")
        val unlimitedImageGenerationLimits: List<Limit> = emptyList()
    )

    @Serializable
    public data class Limit(
        @SerialName("resolution")
        val resolution: Int,
        @SerialName("maxPrompts")
        val maxPriorityActions: Int
    )

    @Serializable
    public data class TrainingStepsLeft(
        @SerialName("fixedTrainingStepsLeft")
        val fixedTrainingStepsLeft: Int = 0,
        @SerialName("purchasedTrainingSteps")
        val purchasedTrainingSteps: Int = 0
    )
}