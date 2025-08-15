package com.xeniac.chillclub.feature_settings.domain.use_cases

import com.xeniac.chillclub.core.domain.models.IsBackgroundPlayEnabled
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetIsPlayInBackgroundEnabledUseCase(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    operator fun invoke(): Flow<IsBackgroundPlayEnabled> =
        settingsDataStoreRepository.isPlayInBackgroundEnabled()
}