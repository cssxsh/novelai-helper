package xyz.cssxsh.novelai.user

import kotlinx.serialization.*

@Serializable
public data class Priority(
    @SerialName("maxPriorityActions")
    val maxPriorityActions: Int = 0,
    @SerialName("nextRefillAt")
    val nextRefillAt: Int = 0,
    @SerialName("taskPriority")
    val taskPriority: Int = 0
)