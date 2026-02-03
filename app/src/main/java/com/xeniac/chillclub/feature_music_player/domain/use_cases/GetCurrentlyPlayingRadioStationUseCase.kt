package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

@ViewModelScoped
class GetCurrentlyPlayingRadioStationUseCase @Inject constructor(
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