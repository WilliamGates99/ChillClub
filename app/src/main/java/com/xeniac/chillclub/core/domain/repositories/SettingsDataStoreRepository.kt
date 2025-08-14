package com.xeniac.chillclub.core.domain.repositories

import com.xeniac.chillclub.core.domain.models.AppLocale
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.models.IsBackgroundPlayEnabled
import kotlinx.coroutines.flow.Flow

typealias IsActivityRestartNeeded = Boolean

interface SettingsDataStoreRepository {

    fun getCurrentAppThemeSynchronously(): AppTheme

    fun getCurrentAppTheme(): Flow<AppTheme>

    suspend fun storeCurrentAppTheme(appTheme: AppTheme)

    fun getCurrentAppLocale(): AppLocale

    suspend fun storeCurrentAppLocale(newAppLocale: AppLocale): IsActivityRestartNeeded

    fun isPlayInBackgroundEnabled(): Flow<IsBackgroundPlayEnabled>

    suspend fun isPlayInBackgroundEnabled(isEnabled: Boolean)
}