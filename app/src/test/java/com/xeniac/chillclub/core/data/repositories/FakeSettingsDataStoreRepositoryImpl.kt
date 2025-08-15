package com.xeniac.chillclub.core.data.repositories

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xeniac.chillclub.core.domain.models.AppLocale
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.models.IsBackgroundPlayEnabled
import com.xeniac.chillclub.core.domain.repositories.IsActivityRestartNeeded
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import kotlinx.coroutines.flow.Flow

class FakeSettingsDataStoreRepositoryImpl : SettingsDataStoreRepository {

    var currentAppTheme = SnapshotStateList<AppTheme>().apply { add(AppTheme.Dark) }
    var currentLocale: AppLocale = AppLocale.Default
    var isPlayInBackgroundEnabled = SnapshotStateList<Boolean>().apply { add(true) }

    override fun getCurrentAppThemeSynchronously(): AppTheme = currentAppTheme.first()

    override fun getCurrentAppTheme(): Flow<AppTheme> = snapshotFlow {
        currentAppTheme.first()
    }

    override suspend fun storeCurrentAppTheme(appTheme: AppTheme) {
        currentAppTheme.apply {
            clear()
            add(appTheme)
        }
    }

    override fun getCurrentAppLocale(): AppLocale = currentLocale

    override suspend fun storeCurrentAppLocale(
        newAppLocale: AppLocale
    ): IsActivityRestartNeeded {
        val isActivityRestartNeeded = isActivityRestartNeeded(newAppLocale)

        currentLocale = newAppLocale

        return isActivityRestartNeeded
    }

    fun isActivityRestartNeeded(
        newLocale: AppLocale
    ): Boolean = currentLocale.layoutDirectionCompose != newLocale.layoutDirectionCompose

    override fun isPlayInBackgroundEnabled(): Flow<IsBackgroundPlayEnabled> = snapshotFlow {
        isPlayInBackgroundEnabled.first()
    }

    override suspend fun isPlayInBackgroundEnabled(isEnabled: Boolean) {
        isPlayInBackgroundEnabled.apply {
            clear()
            add(isEnabled)
        }
    }
}