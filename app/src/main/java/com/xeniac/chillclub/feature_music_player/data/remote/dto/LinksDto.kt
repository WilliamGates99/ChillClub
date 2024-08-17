package com.xeniac.chillclub.feature_music_player.data.remote.dto

import com.xeniac.chillclub.feature_music_player.domain.models.Links
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LinksDto(
    @SerialName("youtube")
    val youtube: String
) {
    fun toLinks(): Links = Links(
        youtube = youtube
    )
}