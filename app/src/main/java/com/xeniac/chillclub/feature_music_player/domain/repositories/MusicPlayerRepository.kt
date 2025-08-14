package com.xeniac.chillclub.feature_music_player.domain.repositories

import com.xeniac.chillclub.BuildConfig
import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.core.domain.models.Result
import com.xeniac.chillclub.feature_music_player.domain.utils.AdjustVolumeError
import com.xeniac.chillclub.feature_music_player.domain.utils.GetRadioStationsError
import kotlinx.coroutines.flow.Flow

typealias MusicVolumePercentage = Float

interface MusicPlayerRepository {

    fun observeMusicVolumeChanges(): Flow<MusicVolumePercentage>

    fun adjustMusicVolume(
        newPercentage: MusicVolumePercentage,
        currentPercentage: MusicVolumePercentage
    ): Flow<Result<Unit, AdjustVolumeError>>

    fun getRadioStations(
        fetchFromRemote: Boolean
    ): Flow<Result<List<RadioStation>, GetRadioStationsError>>

    fun getCurrentlyPlayingRadioStation(
        radioStationId: Long
    ): Flow<RadioStation?>

    sealed class EndPoints(val url: String) {
        data object GetRadioStations : EndPoints(
            url = "${BuildConfig.KTOR_HTTP_BASE_URL}/radio.json"
        )
    }
}