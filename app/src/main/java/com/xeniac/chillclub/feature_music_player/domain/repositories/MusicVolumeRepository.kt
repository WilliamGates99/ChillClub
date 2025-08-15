package com.xeniac.chillclub.feature_music_player.domain.repositories

import com.xeniac.chillclub.core.domain.models.Result
import com.xeniac.chillclub.feature_music_player.domain.errors.AdjustVolumeError
import kotlinx.coroutines.flow.Flow

typealias MusicVolumePercentage = Float

interface MusicVolumeRepository {

    fun observeMusicVolumeChanges(): Flow<MusicVolumePercentage>

    suspend fun adjustMusicVolume(
        newPercentage: MusicVolumePercentage,
        currentPercentage: MusicVolumePercentage
    ): Result<Unit, AdjustVolumeError>
}