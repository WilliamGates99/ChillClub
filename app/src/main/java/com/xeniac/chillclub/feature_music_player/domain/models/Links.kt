package com.xeniac.chillclub.feature_music_player.domain.models

import android.os.Parcelable
import com.xeniac.chillclub.feature_music_player.data.remote.dto.LinksDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class Links(
    val youtube: String
) : Parcelable {
    fun toLinksDto(): LinksDto = LinksDto(
        youtube = youtube
    )
}