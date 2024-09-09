package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import com.xeniac.chillclub.feature_music_player.domain.utils.AdjustVolumeError
import kotlinx.coroutines.flow.Flow

class IncreaseMusicVolumeUseCase(
    private val musicPlayerRepository: MusicPlayerRepository
) {
    operator fun invoke(): Flow<Result<Unit, AdjustVolumeError>> =
        musicPlayerRepository.increaseMusicVolume()
}