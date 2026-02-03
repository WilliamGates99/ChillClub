package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.core.domain.models.Result
import com.xeniac.chillclub.feature_music_player.domain.errors.GetRadioStationsError
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetRadioStationsUseCase @Inject constructor(
    private val musicPlayerRepository: MusicPlayerRepository
) {
    operator fun invoke(
        fetchFromRemote: Boolean
    ): Flow<Result<List<RadioStation>, GetRadioStationsError>> =
        musicPlayerRepository.getRadioStations(
            fetchFromRemote = fetchFromRemote
        )
}