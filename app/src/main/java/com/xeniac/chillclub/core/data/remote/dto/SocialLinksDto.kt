package com.xeniac.chillclub.core.data.remote.dto

import com.xeniac.chillclub.core.domain.models.SocialLinks
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SocialLinksDto(
    @SerialName("youtube")
    val youtube: String
) {
    fun toSocialLinks(): SocialLinks = SocialLinks(
        youtube = youtube
    )
}