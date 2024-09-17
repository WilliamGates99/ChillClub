package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetNotificationPermissionCountUseCase(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    operator fun invoke(): Flow<Int> = settingsDataStoreRepository.getNotificationPermissionCount()
}