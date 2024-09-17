package com.xeniac.chillclub.feature_settings.domain.use_cases

import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentAppThemeUseCase(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    operator fun invoke(): Flow<AppTheme> = settingsDataStoreRepository.getCurrentAppTheme()
}