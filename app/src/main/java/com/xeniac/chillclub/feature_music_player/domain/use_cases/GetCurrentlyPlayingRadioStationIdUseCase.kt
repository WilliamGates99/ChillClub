package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository
import com.xeniac.chillclub.core.domain.repositories.RadioStationId
import kotlinx.coroutines.flow.Flow

class GetCurrentlyPlayingRadioStationIdUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<RadioStationId?> =
        preferencesRepository.getCurrentlyPlayingRadioStationId()
}