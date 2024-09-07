package com.xeniac.chillclub.feature_music_player.domain.repositories

import com.xeniac.chillclub.BuildConfig
import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.feature_music_player.domain.utils.AdjustVolumeError
import com.xeniac.chillclub.feature_music_player.domain.utils.GetRadioStationsError
import kotlinx.coroutines.flow.Flow

typealias MusicVolumePercentage = Float

interface MusicPlayerRepository {

    fun observeMusicVolumeChanges(): Flow<MusicVolumePercentage>

    suspend fun decreaseMusicVolume(): Flow<Result<Unit, AdjustVolumeError>>

    suspend fun increaseMusicVolume(): Flow<Result<Unit, AdjustVolumeError>>

    suspend fun getRadioStations(
        fetchFromRemote: Boolean
    ): Flow<Result<List<RadioStation>, GetRadioStationsError>>

    sealed class EndPoints(val url: String) {
        data object GetRadioStations : EndPoints(
            url = "${BuildConfig.KTOR_HTTP_BASE_URL}/radio.json"
        )
    }
}