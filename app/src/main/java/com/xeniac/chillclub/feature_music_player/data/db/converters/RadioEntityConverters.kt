package com.xeniac.chillclub.feature_music_player.data.db.converters

import androidx.room.TypeConverter
import com.xeniac.chillclub.feature_music_player.data.remote.dto.ChannelDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RadioEntityConverters {

    @TypeConverter
    fun fromChannelDto(channelDto: ChannelDto): String = Json.encodeToString(channelDto)

    @TypeConverter
    fun toChannelDto(encodedJson: String): ChannelDto = Json.decodeFromString(encodedJson)

    @TypeConverter
    fun fromTags(tags: List<String>): String = Json.encodeToString(tags)

    @TypeConverter
    fun toTags(encodedJson: String): List<String> = Json.decodeFromString(encodedJson)
}