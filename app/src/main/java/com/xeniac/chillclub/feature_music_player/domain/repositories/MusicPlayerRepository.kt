package com.xeniac.chillclub.feature_music_player.domain.repositories

import com.xeniac.chillclub.BuildConfig
import com.xeniac.chillclub.core.domain.models.Radio
import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.feature_music_player.domain.utils.AdjustVolumeError
import com.xeniac.chillclub.feature_music_player.domain.utils.GetRadiosError
import kotlinx.coroutines.flow.Flow

typealias MusicVolume = Int

interface MusicPlayerRepository {

    fun observeMusicVolumeChanges(): Flow<MusicVolume>

    suspend fun decreaseMusicVolume(): Flow<Result<Unit, AdjustVolumeError>>

    suspend fun increaseMusicVolume(): Flow<Result<Unit, AdjustVolumeError>>

    suspend fun getRadios(
        fetchFromRemote: Boolean
    ): Flow<Result<List<Radio>, GetRadiosError>>

    sealed class EndPoints(val url: String) {
        data object GetRadios : EndPoints(url = "${BuildConfig.KTOR_HTTP_BASE_URL}/radio.json")
    }
}