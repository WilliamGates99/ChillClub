package com.xeniac.chillclub.feature_settings.domain.use_cases

import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentAppThemeUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<AppTheme> = preferencesRepository.getCurrentAppTheme()
}