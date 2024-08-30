package com.xeniac.chillclub.feature_music_player.data.remote.dto

import com.xeniac.chillclub.core.data.remote.dto.RadioDto
import com.xeniac.chillclub.feature_music_player.domain.models.GetRadiosResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRadiosResponseDto(
    @SerialName("data")
    val radioDtos: List<RadioDto>
) {
    fun toGetRadiosResponse(): GetRadiosResponse = GetRadiosResponse(
        radios = radioDtos.map { it.toRadio() }
    )
}