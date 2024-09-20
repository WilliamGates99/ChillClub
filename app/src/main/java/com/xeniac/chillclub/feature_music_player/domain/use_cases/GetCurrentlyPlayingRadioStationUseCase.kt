package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class GetCurrentlyPlayingRadioStationUseCase(
    private val musicPlayerRepository: MusicPlayerRepository
) {
    operator fun invoke(
        radioStationId: Long?
    ): Flow<RadioStation?> {
        if (radioStationId == null) {
            return emptyFlow()
        }

        return musicPlayerRepository.getCurrentlyPlayingRadioStation(
            radioStationId = radioStationId
        )
    }
}