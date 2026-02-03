package com.xeniac.chillclub.feature_settings.domain.use_cases

import com.xeniac.chillclub.core.domain.models.IsBackgroundPlayEnabled
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetIsPlayInBackgroundEnabledUseCase @Inject constructor(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    operator fun invoke(): Flow<IsBackgroundPlayEnabled> =
        settingsDataStoreRepository.isPlayInBackgroundEnabled()
}