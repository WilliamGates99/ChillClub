package com.xeniac.chillclub.core.domain.use_cases

import com.xeniac.chillclub.core.domain.models.AppLocale
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetCurrentAppLocaleUseCase @Inject constructor(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    operator fun invoke(): AppLocale = settingsDataStoreRepository.getCurrentAppLocale()
}