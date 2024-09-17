package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository

class StoreNotificationPermissionCountUseCase(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    suspend operator fun invoke(
        count: Int
    ) = settingsDataStoreRepository.storeNotificationPermissionCount(count)
}