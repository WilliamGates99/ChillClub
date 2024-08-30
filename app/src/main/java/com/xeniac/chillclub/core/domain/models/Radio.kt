package com.xeniac.chillclub.core.domain.models

import android.os.Parcelable
import com.xeniac.chillclub.core.data.local.entities.RadioEntity
import com.xeniac.chillclub.core.data.remote.dto.RadioDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class Radio(
    val youtubeVideoId: String,
    val title: String,
    val channel: Channel,
    val category: String,
    val tags: List<String>
) : Parcelable {
    fun toRadioEntity(): RadioEntity = RadioEntity(
        youtubeVideoId = youtubeVideoId,
        title = title,
        channel = channel,
        category = category,
        tags = tags
    )

    fun toRadioDto(): RadioDto = RadioDto(
        youtubeVideoId = youtubeVideoId,
        title = title,
        channelDto = channel.toChannelDto(),
        category = category,
        tags = tags
    )
}