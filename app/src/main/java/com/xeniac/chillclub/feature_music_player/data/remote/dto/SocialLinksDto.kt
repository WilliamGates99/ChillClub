package com.xeniac.chillclub.feature_music_player.data.remote.dto

import com.xeniac.chillclub.feature_music_player.domain.models.SocialLinks
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