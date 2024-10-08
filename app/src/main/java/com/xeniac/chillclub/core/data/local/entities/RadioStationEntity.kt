package com.xeniac.chillclub.core.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xeniac.chillclub.core.data.remote.dto.RadioStationDto
import com.xeniac.chillclub.core.data.utils.DateHelper
import com.xeniac.chillclub.core.domain.models.Channel
import com.xeniac.chillclub.core.domain.models.RadioStation

@Entity(tableName = "radio_stations")
data class RadioStationEntity(
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
    fun toRadioStationDto(): RadioStationDto = RadioStationDto(
        youtubeVideoId = youtubeVideoId,
        title = title,
        channelDto = channel.toChannelDto(),
        category = category,
        tags = tags
    )

    fun toRadioStation(): RadioStation = RadioStation(
        youtubeVideoId = youtubeVideoId,
        title = title,
        channel = channel,
        category = category,
        tags = tags,
        id = id
    )
}