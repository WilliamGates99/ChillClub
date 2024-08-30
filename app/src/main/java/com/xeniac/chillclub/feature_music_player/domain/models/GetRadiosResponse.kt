package com.xeniac.chillclub.feature_music_player.domain.models

import android.os.Parcelable
import com.xeniac.chillclub.core.domain.models.Radio
import com.xeniac.chillclub.feature_music_player.data.remote.dto.GetRadiosResponseDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetRadiosResponse(
    val radios: List<Radio>
) : Parcelable {
    fun toGetRadiosResponseDto(): GetRadiosResponseDto = GetRadiosResponseDto(
        radioDtos = radios.map { it.toRadioDto() }
    )
}