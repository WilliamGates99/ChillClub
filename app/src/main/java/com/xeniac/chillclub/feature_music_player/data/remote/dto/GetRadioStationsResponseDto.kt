package com.xeniac.chillclub.feature_music_player.data.remote.dto

import com.xeniac.chillclub.core.data.remote.dto.RadioStationDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRadioStationsResponseDto(
    @SerialName("version")
    val version: Int,
    @SerialName("data")
    val radioStationDtos: List<RadioStationDto>
)