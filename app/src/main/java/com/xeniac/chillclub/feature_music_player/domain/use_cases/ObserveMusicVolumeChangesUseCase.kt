package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumePercentage
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumeRepository
import kotlinx.coroutines.flow.Flow

class ObserveMusicVolumeChangesUseCase(
    private val musicVolumeRepository: MusicVolumeRepository
) {
    operator fun invoke(): Flow<MusicVolumePercentage> =
        musicVolumeRepository.observeMusicVolumeChanges()
}