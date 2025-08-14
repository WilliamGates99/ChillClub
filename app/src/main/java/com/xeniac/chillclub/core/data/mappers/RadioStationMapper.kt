package com.xeniac.chillclub.core.data.mappers

import com.xeniac.chillclub.core.data.local.entities.RadioStationEntity
import com.xeniac.chillclub.core.data.remote.dto.RadioStationDto
import com.xeniac.chillclub.core.domain.models.RadioStation

fun RadioStationDto.toRadioStationEntity(): RadioStationEntity = RadioStationEntity(
    youtubeVideoId = youtubeVideoId,
    title = title,
    channel = channelDto.toChannel(),
    category = category,
    tags = tags
)

fun RadioStationDto.toRadioStation(): RadioStation = RadioStation(
    youtubeVideoId = youtubeVideoId,
    title = title,
    channel = channelDto.toChannel(),
    category = category,
    tags = tags
)

fun RadioStationEntity.toRadioStationDto(): RadioStationDto = RadioStationDto(
    youtubeVideoId = youtubeVideoId,
    title = title,
    channelDto = channel.toChannelDto(),
    category = category,
    tags = tags
)

fun RadioStationEntity.toRadioStation(): RadioStation = RadioStation(
    youtubeVideoId = youtubeVideoId,
    title = title,
    channel = channel,
    category = category,
    tags = tags,
    id = id
)

fun RadioStation.toRadioStationEntity(): RadioStationEntity = RadioStationEntity(
    youtubeVideoId = youtubeVideoId,
    title = title,
    channel = channel,
    category = category,
    tags = tags,
    id = id
)

fun RadioStation.toRadioStationDto(): RadioStationDto = RadioStationDto(
    youtubeVideoId = youtubeVideoId,
    title = title,
    channelDto = channel.toChannelDto(),
    category = category,
    tags = tags
)