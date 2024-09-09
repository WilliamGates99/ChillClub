package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import com.xeniac.chillclub.feature_music_player.domain.utils.GetRadioStationsError
import kotlinx.coroutines.flow.Flow

class GetRadioStationsUseCase(
    private val musicPlayerRepository: MusicPlayerRepository
) {
    operator fun invoke(
        fetchFromRemote: Boolean
    ): Flow<Result<List<RadioStation>, GetRadioStationsError>> =
        musicPlayerRepository.getRadioStations(fetchFromRemote)
}