package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.models.RadioStationId
import com.xeniac.chillclub.core.domain.repositories.MusicPlayerDataStoreRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetCurrentlyPlayingRadioStationIdUseCase @Inject constructor(
    private val musicPlayerDataStoreRepository: MusicPlayerDataStoreRepository
) {
    operator fun invoke(): Flow<RadioStationId?> =
        musicPlayerDataStoreRepository.getCurrentlyPlayingRadioStationId()
}