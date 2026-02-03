package com.xeniac.chillclub.feature_settings.domain.use_cases

import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.models.Result
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import com.xeniac.chillclub.feature_settings.domain.errors.StoreCurrentAppThemeError
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class StoreCurrentAppThemeUseCase @Inject constructor(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    operator fun invoke(
        newAppTheme: AppTheme
    ): Flow<Result<Unit, StoreCurrentAppThemeError>> = flow {
        return@flow try {
            settingsDataStoreRepository.storeCurrentAppTheme(newAppTheme)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(StoreCurrentAppThemeError.SomethingWentWrong))
        }
    }
}