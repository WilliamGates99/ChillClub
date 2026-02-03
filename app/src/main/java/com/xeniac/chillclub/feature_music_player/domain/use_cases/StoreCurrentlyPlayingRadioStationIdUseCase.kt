package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.models.Result
import com.xeniac.chillclub.core.domain.repositories.MusicPlayerDataStoreRepository
import com.xeniac.chillclub.feature_music_player.domain.errors.StoreCurrentlyPlayingRadioStationIdError
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class StoreCurrentlyPlayingRadioStationIdUseCase @Inject constructor(
    private val musicPlayerDataStoreRepository: MusicPlayerDataStoreRepository
) {
    operator fun invoke(
        radioStationId: Long
    ): Flow<Result<Unit, StoreCurrentlyPlayingRadioStationIdError>> = flow {
        return@flow try {
            musicPlayerDataStoreRepository.storeCurrentlyPlayingRadioStationId(id = radioStationId)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(StoreCurrentlyPlayingRadioStationIdError.SomethingWentWrong))
        }
    }
}