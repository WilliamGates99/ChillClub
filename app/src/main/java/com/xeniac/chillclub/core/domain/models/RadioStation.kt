package com.xeniac.chillclub.core.domain.models

import android.os.Parcelable
import com.xeniac.chillclub.core.data.local.entities.RadioStationEntity
import com.xeniac.chillclub.core.data.remote.dto.RadioStationDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class RadioStation(
    val youtubeVideoId: String,
    val title: String,
    val channel: Channel,
    val category: String,
    val tags: List<String>,
    val id: Long? = null
) : Parcelable {
    fun toRadioStationEntity(): RadioStationEntity = RadioStationEntity(
        youtubeVideoId = youtubeVideoId,
        title = title,
        channel = channel,
        category = category,
        tags = tags,
        id = id
    )

    fun toRadioStationDto(): RadioStationDto = RadioStationDto(
        youtubeVideoId = youtubeVideoId,
        title = title,
        channelDto = channel.toChannelDto(),
        category = category,
        tags = tags
    )
}