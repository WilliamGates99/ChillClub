package com.xeniac.chillclub.feature_settings.domain.use_cases

import com.xeniac.chillclub.core.domain.models.Result
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import com.xeniac.chillclub.feature_settings.domain.errors.StoreIsPlayInBackgroundEnabledError

class StoreIsPlayInBackgroundEnabledUseCase(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    suspend operator fun invoke(
        isEnabled: Boolean
    ): Result<Unit, StoreIsPlayInBackgroundEnabledError> = try {
        settingsDataStoreRepository.isPlayInBackgroundEnabled(isEnabled = isEnabled)
        Result.Success(Unit)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(StoreIsPlayInBackgroundEnabledError.SomethingWentWrong)
    }
}