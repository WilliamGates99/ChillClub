package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumePercentage
import kotlinx.coroutines.flow.Flow

class ObserveMusicVolumeChangesUseCase(
    private val musicPlayerRepository: MusicPlayerRepository
) {
    operator fun invoke(): Flow<MusicVolumePercentage> =
        musicPlayerRepository.observeMusicVolumeChanges()
}