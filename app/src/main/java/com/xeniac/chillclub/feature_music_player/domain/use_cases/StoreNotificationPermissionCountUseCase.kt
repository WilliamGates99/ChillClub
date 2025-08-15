package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.repositories.PermissionsDataStoreRepository

class StoreNotificationPermissionCountUseCase(
    private val permissionsDataStoreRepository: PermissionsDataStoreRepository
) {
    suspend operator fun invoke(
        count: Int
    ) = permissionsDataStoreRepository.storeNotificationPermissionCount(count)
}