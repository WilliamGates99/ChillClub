package com.xeniac.chillclub.core.data.repositories

import com.xeniac.chillclub.core.domain.repositories.PermissionsDataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePermissionsDataStoreRepositoryImpl : PermissionsDataStoreRepository {

    var notificationPermissionCount = 0

    override fun getNotificationPermissionCount(): Flow<Int> = flow {
        emit(notificationPermissionCount)
    }

    override suspend fun storeNotificationPermissionCount(count: Int) {
        notificationPermissionCount = count
    }
}