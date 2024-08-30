package com.xeniac.chillclub.core.data.remote.dto

import com.xeniac.chillclub.core.domain.models.Channel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelDto(
    @SerialName("name")
    val name: String,
    @SerialName("avatar")
    val avatarUrl: String? = null,
    @SerialName("social")
    val socialLinksDto: SocialLinksDto? = null
) {
    fun toChannel(): Channel = Channel(
        name = name,
        avatarUrl = avatarUrl,
        socialLinks = socialLinksDto?.toSocialLinks()
    )
}