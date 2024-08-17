package com.xeniac.chillclub.feature_music_player.data.remote.dto

import com.xeniac.chillclub.feature_music_player.domain.models.Radio
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RadioDto(
    @SerialName("id")
    val youtubeVideoId: String,
    @SerialName("title")
    val title: String,
    @SerialName("author")
    val channelDto: ChannelDto,
    @SerialName("category")
    val category: String,
    @SerialName("tags")
    val tags: List<String>
) {
    fun toRadio(): Radio = Radio(
        youtubeVideoId = youtubeVideoId,
        title = title,
        channel = channelDto.toChannel(),
        category = category,
        tags = tags
    )
}