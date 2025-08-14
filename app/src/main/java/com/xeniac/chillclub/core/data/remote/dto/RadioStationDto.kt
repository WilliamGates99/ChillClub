package com.xeniac.chillclub.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RadioStationDto(
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
)