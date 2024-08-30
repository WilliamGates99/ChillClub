package com.xeniac.chillclub.core.data.remote.dto

import com.xeniac.chillclub.core.data.local.entities.RadioEntity
import com.xeniac.chillclub.core.domain.models.Radio
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
    fun toRadioEntity(): RadioEntity = RadioEntity(
        youtubeVideoId = youtubeVideoId,
        title = title,
        channel = channelDto.toChannel(),
        category = category,
        tags = tags
    )

    fun toRadio(): Radio = Radio(
        youtubeVideoId = youtubeVideoId,
        title = title,
        channel = channelDto.toChannel(),
        category = category,
        tags = tags
    )
}