package com.xeniac.chillclub.feature_music_player.domain.models

import android.os.Parcelable
import com.xeniac.chillclub.feature_music_player.data.db.entities.RadioEntity
import com.xeniac.chillclub.feature_music_player.data.remote.dto.RadioDto
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
        channelDto = channel.toChannelDto(),
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