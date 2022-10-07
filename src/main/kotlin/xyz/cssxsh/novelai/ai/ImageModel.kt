package xyz.cssxsh.novelai.ai

import kotlinx.serialization.*

@Serializable
public enum class ImageModel {
    @SerialName("stable-diffusion")
    STABLE_DIFFUSION,
    @SerialName("nai-diffusion")
    NAI_DIFFUSION,
    @SerialName("safe-diffusion")
    SAFE_DIFFUSION,
    @SerialName("nai-diffusion-furry")
    NAI_DIFFUSION_FURRY
}