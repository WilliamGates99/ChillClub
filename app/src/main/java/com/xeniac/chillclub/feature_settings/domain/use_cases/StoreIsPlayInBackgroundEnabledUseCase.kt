package com.xeniac.chillclub.feature_settings.domain.use_cases

import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.feature_settings.domain.utils.PlayInBackgroundError

class StoreIsPlayInBackgroundEnabledUseCase(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    suspend operator fun invoke(isEnabled: Boolean): Result<Unit, PlayInBackgroundError> = try {
        settingsDataStoreRepository.isPlayInBackgroundEnabled(isEnabled = isEnabled)
        Result.Success(Unit)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(PlayInBackgroundError.SomethingWentWrong)
    }
}