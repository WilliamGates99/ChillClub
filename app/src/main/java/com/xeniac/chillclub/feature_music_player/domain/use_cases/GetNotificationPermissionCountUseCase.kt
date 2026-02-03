package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.repositories.PermissionsDataStoreRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetNotificationPermissionCountUseCase @Inject constructor(
    private val permissionsDataStoreRepository: PermissionsDataStoreRepository
) {
    operator fun invoke(): Flow<Int> =
        permissionsDataStoreRepository.getNotificationPermissionCount()
}