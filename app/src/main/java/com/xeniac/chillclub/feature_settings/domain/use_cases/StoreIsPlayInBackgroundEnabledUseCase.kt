package com.xeniac.chillclub.feature_settings.domain.use_cases

import com.xeniac.chillclub.core.domain.models.Result
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import com.xeniac.chillclub.feature_settings.domain.errors.StoreIsPlayInBackgroundEnabledError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StoreIsPlayInBackgroundEnabledUseCase(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    operator fun invoke(
        isEnabled: Boolean
    ): Flow<Result<Unit, StoreIsPlayInBackgroundEnabledError>> = flow {
        return@flow try {
            settingsDataStoreRepository.isPlayInBackgroundEnabled(isEnabled = isEnabled)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(StoreIsPlayInBackgroundEnabledError.SomethingWentWrong))
        }
    }
}