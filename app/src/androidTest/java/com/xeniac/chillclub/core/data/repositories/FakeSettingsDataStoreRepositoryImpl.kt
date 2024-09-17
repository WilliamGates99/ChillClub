package com.xeniac.chillclub.core.data.repositories

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xeniac.chillclub.core.domain.models.AppLocale
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.repositories.IsActivityRestartNeeded
import com.xeniac.chillclub.core.domain.repositories.IsBackgroundPlayEnabled
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeSettingsDataStoreRepositoryImpl @Inject constructor() : SettingsDataStoreRepository {

    var currentAppTheme: AppTheme = AppTheme.Dark
    var currentLocale: AppLocale = AppLocale.Default
    var isPlayInBackgroundEnabled = SnapshotStateList<Boolean>().apply { add(true) }
    var notificationPermissionCount = 0

    override fun getCurrentAppThemeSynchronously(): AppTheme = currentAppTheme

    override fun getCurrentAppTheme(): Flow<AppTheme> = flow { emit(currentAppTheme) }

    override fun getCurrentAppLocale(): AppLocale = currentLocale

    override fun isPlayInBackgroundEnabled(): Flow<IsBackgroundPlayEnabled> = snapshotFlow {
        isPlayInBackgroundEnabled.first()
    }

    override fun getNotificationPermissionCount(): Flow<Int> = flow {
        emit(notificationPermissionCount)
    }

    override suspend fun storeCurrentAppTheme(appTheme: AppTheme) {
        currentAppTheme = appTheme
    }

    override suspend fun storeCurrentAppLocale(
        newAppLocale: AppLocale
    ): IsActivityRestartNeeded {
        val isActivityRestartNeeded = isActivityRestartNeeded(newAppLocale)

        currentLocale = newAppLocale

        return isActivityRestartNeeded
    }

    override suspend fun isPlayInBackgroundEnabled(isEnabled: Boolean) {
        isPlayInBackgroundEnabled.apply {
            clear()
            add(isEnabled)
        }
    }

    override suspend fun storeNotificationPermissionCount(count: Int) {
        notificationPermissionCount = count
    }

    fun isActivityRestartNeeded(
        newLocale: AppLocale
    ): Boolean = currentLocale.layoutDirectionCompose != newLocale.layoutDirectionCompose
}