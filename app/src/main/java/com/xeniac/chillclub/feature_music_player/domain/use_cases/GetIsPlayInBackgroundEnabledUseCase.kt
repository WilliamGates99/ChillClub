package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.repositories.IsBackgroundPlayEnabled
import com.xeniac.chillclub.core.domain.repositories.MiscellaneousDataStoreRepository
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetIsPlayInBackgroundEnabledUseCase(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    operator fun invoke(): Flow<IsBackgroundPlayEnabled> = settingsDataStoreRepository.isPlayInBackgroundEnabled()
}