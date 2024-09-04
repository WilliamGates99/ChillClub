package com.xeniac.chillclub.core.data.remote.dto

import com.xeniac.chillclub.core.data.local.entities.RadioStationEntity
import com.xeniac.chillclub.core.domain.models.RadioStation
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
) {
    fun toRadioStationEntity(): RadioStationEntity = RadioStationEntity(
        youtubeVideoId = youtubeVideoId,
        title = title,
        channel = channelDto.toChannel(),
        category = category,
        tags = tags
    )

    fun toRadioStation(): RadioStation = RadioStation(
        youtubeVideoId = youtubeVideoId,
        title = title,
        channel = channelDto.toChannel(),
        category = category,
        tags = tags
    )
}