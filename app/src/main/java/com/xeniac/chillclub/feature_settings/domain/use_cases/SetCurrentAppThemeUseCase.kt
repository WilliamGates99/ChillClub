package com.xeniac.chillclub.feature_settings.domain.use_cases

import com.xeniac.chillclub.core.data.mapper.toAppThemeDto
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository
import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.feature_settings.domain.utils.AppThemeError

class SetCurrentAppThemeUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(newAppTheme: AppTheme): Result<Unit, AppThemeError> = try {
        preferencesRepository.setCurrentAppTheme(appThemeDto = newAppTheme.toAppThemeDto())
        Result.Success(Unit)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(AppThemeError.SomethingWentWrong)
    }
}