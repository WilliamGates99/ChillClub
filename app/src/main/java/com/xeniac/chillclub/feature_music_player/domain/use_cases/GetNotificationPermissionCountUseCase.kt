package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.repositories.PermissionsDataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetNotificationPermissionCountUseCase(
    private val permissionsDataStoreRepository: PermissionsDataStoreRepository
) {
    operator fun invoke(): Flow<Int> =
        permissionsDataStoreRepository.getNotificationPermissionCount()
}