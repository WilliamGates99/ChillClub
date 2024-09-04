package com.xeniac.chillclub.core.data.local.converters

import androidx.room.TypeConverter
import com.xeniac.chillclub.core.domain.models.Channel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RadioStationEntityConverters {

    @TypeConverter
    fun fromChannel(channel: Channel): String = Json.encodeToString(channel)

    @TypeConverter
    fun toChannel(encodedJson: String): Channel = Json.decodeFromString(encodedJson)

    @TypeConverter
    fun fromTags(tags: List<String>): String = Json.encodeToString(tags)

    @TypeConverter
    fun toTags(encodedJson: String): List<String> = Json.decodeFromString(encodedJson)
}