package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.models.RadioStationId
import com.xeniac.chillclub.core.domain.repositories.MusicPlayerDataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentlyPlayingRadioStationIdUseCase(
    private val musicPlayerDataStoreRepository: MusicPlayerDataStoreRepository
) {
    operator fun invoke(): Flow<RadioStationId?> =
        musicPlayerDataStoreRepository.getCurrentlyPlayingRadioStationId()
}