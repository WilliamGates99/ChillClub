package com.xeniac.chillclub.core.domain.repositories

import com.xeniac.chillclub.core.domain.models.AppLocale
import com.xeniac.chillclub.core.domain.models.AppTheme
import kotlinx.coroutines.flow.Flow

typealias IsBackgroundPlayEnabled = Boolean
typealias IsActivityRestartNeeded = Boolean

interface SettingsDataStoreRepository {

    fun getCurrentAppThemeSynchronously(): AppTheme

    fun getCurrentAppTheme(): Flow<AppTheme>

    fun getCurrentAppLocale(): AppLocale

    fun isPlayInBackgroundEnabled(): Flow<IsBackgroundPlayEnabled>

    fun getNotificationPermissionCount(): Flow<Int>

    suspend fun storeCurrentAppTheme(appTheme: AppTheme)

    suspend fun storeCurrentAppLocale(newAppLocale: AppLocale): IsActivityRestartNeeded

    suspend fun isPlayInBackgroundEnabled(isEnabled: Boolean)

    suspend fun storeNotificationPermissionCount(count: Int)
}