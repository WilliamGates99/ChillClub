package com.xeniac.chillclub.feature_music_player.domain.models

import android.os.Parcelable
import com.xeniac.chillclub.feature_music_player.data.remote.dto.SocialLinksDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class SocialLinks(
    val youtube: String
) : Parcelable {
    fun toSocialLinksDto(): SocialLinksDto = SocialLinksDto(
        youtube = youtube
    )
}