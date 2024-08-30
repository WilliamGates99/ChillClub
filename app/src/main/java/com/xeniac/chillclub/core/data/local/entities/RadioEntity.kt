package com.xeniac.chillclub.core.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xeniac.chillclub.core.data.remote.dto.RadioDto
import com.xeniac.chillclub.core.data.utils.DateHelper
import com.xeniac.chillclub.core.domain.models.Channel
import com.xeniac.chillclub.core.domain.models.Radio

@Entity(tableName = "radios")
data class RadioEntity(
    @ColumnInfo(name = "youtube_video_id")
    val youtubeVideoId: String,
    val title: String,
    val channel: Channel,
    val category: String,
    val tags: List<String>,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = DateHelper.getCurrentTimeInSeconds(),
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
) {
    fun toRadioDto(): RadioDto = RadioDto(
        youtubeVideoId = youtubeVideoId,
        title = title,
        channelDto = channel.toChannelDto(),
        category = category,
        tags = tags
    )

    fun toRadio(): Radio = Radio(
        youtubeVideoId = youtubeVideoId,
        title = title,
        channel = channel,
        category = category,
        tags = tags
    )
}