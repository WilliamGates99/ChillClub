package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumePercentage
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumeRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class ObserveMusicVolumeChangesUseCase @Inject constructor(
    private val musicVolumeRepository: MusicVolumeRepository
) {
    operator fun invoke(): Flow<MusicVolumePercentage> =
        musicVolumeRepository.observeMusicVolumeChanges()
}