package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository
import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.feature_music_player.domain.utils.CurrentRadioStationError

class StoreCurrentlyPlayingRadioStationIdUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(
        radioStationId: Long
    ): Result<Unit, CurrentRadioStationError> = try {
        preferencesRepository.storeCurrentlyPlayingRadioStationId(id = radioStationId)
        Result.Success(Unit)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(CurrentRadioStationError.SomethingWentWrong)
    }
}