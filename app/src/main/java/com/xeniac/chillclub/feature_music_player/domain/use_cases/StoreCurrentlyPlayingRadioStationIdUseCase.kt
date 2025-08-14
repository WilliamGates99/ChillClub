package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.repositories.MusicPlayerDataStoreRepository
import com.xeniac.chillclub.core.domain.models.Result
import com.xeniac.chillclub.feature_music_player.domain.utils.CurrentRadioStationError

class StoreCurrentlyPlayingRadioStationIdUseCase(
    private val musicPlayerDataStoreRepository: MusicPlayerDataStoreRepository
) {
    suspend operator fun invoke(
        radioStationId: Long
    ): Result<Unit, CurrentRadioStationError> = try {
        musicPlayerDataStoreRepository.storeCurrentlyPlayingRadioStationId(id = radioStationId)
        Result.Success(Unit)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(CurrentRadioStationError.SomethingWentWrong)
    }
}