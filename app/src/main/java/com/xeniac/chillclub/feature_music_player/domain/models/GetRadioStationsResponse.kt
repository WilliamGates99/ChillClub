package com.xeniac.chillclub.feature_music_player.domain.models

import android.os.Parcelable
import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.feature_music_player.data.remote.dto.GetRadioStationsResponseDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetRadioStationsResponse(
    val version: Int,
    val radioStations: List<RadioStation>
) : Parcelable {
    fun toGetRadioStationsResponseDto(): GetRadioStationsResponseDto = GetRadioStationsResponseDto(
        version = version,
        radioStationDtos = radioStations.map { it.toRadioStationDto() }
    )
}