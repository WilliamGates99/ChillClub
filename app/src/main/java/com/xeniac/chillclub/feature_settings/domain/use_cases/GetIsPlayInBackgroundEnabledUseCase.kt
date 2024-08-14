package com.xeniac.chillclub.feature_settings.domain.use_cases

import com.xeniac.chillclub.core.domain.repositories.IsBackgroundPlayEnabled
import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetIsPlayInBackgroundEnabledUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<IsBackgroundPlayEnabled> =
        preferencesRepository.isPlayInBackgroundEnabled()
}