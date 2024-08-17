package com.xeniac.chillclub.feature_music_player.data.remote.dto

import com.xeniac.chillclub.feature_music_player.domain.models.Channel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelDto(
    @SerialName("name")
    val name: String,
    @SerialName("avatar")
    val avatarUrl: String? = null,
    @SerialName("social")
    val linksDto: LinksDto? = null
) {
    fun toChannel(): Channel = Channel(
        name = name,
        avatarUrl = avatarUrl,
        links = linksDto?.toLinks()
    )
}