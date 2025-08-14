package com.xeniac.chillclub.feature_settings.domain.use_cases

import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.models.Result
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import com.xeniac.chillclub.feature_settings.domain.errors.StoreCurrentAppThemeError

class StoreCurrentAppThemeUseCase(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    suspend operator fun invoke(
        newAppTheme: AppTheme
    ): Result<Unit, StoreCurrentAppThemeError> = try {
        settingsDataStoreRepository.storeCurrentAppTheme(newAppTheme)
        Result.Success(Unit)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(StoreCurrentAppThemeError.SomethingWentWrong)
    }
}