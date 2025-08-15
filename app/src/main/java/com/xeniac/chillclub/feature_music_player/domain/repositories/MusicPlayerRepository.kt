package com.xeniac.chillclub.feature_music_player.domain.repositories

import com.xeniac.chillclub.BuildConfig
import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.core.domain.models.Result
import com.xeniac.chillclub.feature_music_player.domain.errors.GetRadioStationsError
import kotlinx.coroutines.flow.Flow

interface MusicPlayerRepository {

    fun getRadioStations(
        fetchFromRemote: Boolean
    ): Flow<Result<List<RadioStation>, GetRadioStationsError>>

    fun getCurrentlyPlayingRadioStation(
        radioStationId: Long
    ): Flow<RadioStation?>

    sealed class EndPoints(val url: String) {
        data object GetRadioStations : EndPoints(
            url = "${BuildConfig.HTTP_BASE_URL}/radio.json"
        )
    }
}