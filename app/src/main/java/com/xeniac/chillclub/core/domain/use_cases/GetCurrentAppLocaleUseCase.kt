package com.xeniac.chillclub.core.domain.use_cases

import com.xeniac.chillclub.core.domain.models.AppLocale
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository

class GetCurrentAppLocaleUseCase(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    operator fun invoke(): AppLocale = settingsDataStoreRepository.getCurrentAppLocale()
}