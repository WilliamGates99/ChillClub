package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.feature_music_player.domain.models.Radio
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import com.xeniac.chillclub.feature_music_player.domain.utils.GetRadiosError
import kotlinx.coroutines.flow.Flow

class GetRadiosUseCase(
    private val musicPlayerRepository: MusicPlayerRepository
) {
    suspend operator fun invoke(
        fetchFromRemote: Boolean
    ): Flow<Result<List<Radio>, GetRadiosError>> = musicPlayerRepository.getRadios(fetchFromRemote)
}