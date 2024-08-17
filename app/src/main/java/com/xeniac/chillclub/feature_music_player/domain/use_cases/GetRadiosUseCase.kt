package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.feature_music_player.domain.models.Radio
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import com.xeniac.chillclub.feature_music_player.domain.utils.GetRadiosError

class GetRadiosUseCase(
    private val musicPlayerRepository: MusicPlayerRepository
) {
    suspend operator fun invoke(
        minPrice: String? = null,
        maxPrice: String? = null,
        takeAfterDelayInSeconds: Int? = null
    ): Result<List<Radio>, GetRadiosError> = musicPlayerRepository.getRadios()
}