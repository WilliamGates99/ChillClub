package com.xeniac.chillclub.feature_settings.domain.use_cases

import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.feature_settings.domain.utils.AppThemeError

class StoreCurrentAppThemeUseCase(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    suspend operator fun invoke(newAppTheme: AppTheme): Result<Unit, AppThemeError> = try {
        settingsDataStoreRepository.storeCurrentAppTheme(newAppTheme)
        Result.Success(Unit)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(AppThemeError.SomethingWentWrong)
    }
}