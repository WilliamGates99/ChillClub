package com.xeniac.chillclub.core.domain.use_cases

import com.xeniac.chillclub.core.domain.models.AppLocale
import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository

class GetCurrentAppLocaleUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): AppLocale = preferencesRepository.getCurrentAppLocale()
}