package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.models.Result
import com.xeniac.chillclub.feature_music_player.domain.errors.AdjustVolumeError
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumePercentage
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumeRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class AdjustMusicVolumeUseCase @Inject constructor(
    private val musicVolumeRepository: MusicVolumeRepository
) {
    operator fun invoke(
        newPercentage: MusicVolumePercentage,
        currentPercentage: MusicVolumePercentage
    ): Flow<Result<Unit, AdjustVolumeError>> = flow {
        return@flow emit(
            musicVolumeRepository.adjustMusicVolume(
                newPercentage = newPercentage,
                currentPercentage = currentPercentage
            )
        )
    }
}