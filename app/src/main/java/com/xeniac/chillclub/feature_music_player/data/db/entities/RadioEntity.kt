package com.xeniac.chillclub.feature_music_player.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xeniac.chillclub.feature_music_player.data.remote.dto.ChannelDto
import com.xeniac.chillclub.feature_music_player.data.remote.dto.RadioDto
import com.xeniac.chillclub.feature_music_player.domain.models.Radio

@Entity(tableName = "radios")
data class RadioEntity(
    @ColumnInfo(name = "youtube_video_id") val youtubeVideoId: String,
    val title: String,
    @ColumnInfo(name = "channel") val channelDto: ChannelDto,
    val category: String,
    val tags: List<String>,
    @PrimaryKey(autoGenerate = true) val id: Long? = null
) {
    fun toRadioDto(): RadioDto = RadioDto(
        youtubeVideoId = youtubeVideoId,
        title = title,
        channelDto = channelDto,
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