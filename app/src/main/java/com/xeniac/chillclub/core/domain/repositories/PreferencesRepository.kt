package com.xeniac.chillclub.core.domain.repositories

import com.xeniac.chillclub.core.domain.models.AppLocale
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.models.RateAppOption
import kotlinx.coroutines.flow.Flow

typealias IsBackgroundPlayEnabled = Boolean
typealias IsActivityRestartNeeded = Boolean
typealias AppUpdateDialogShowCount = Int
typealias IsAppUpdateDialogShownToday = Boolean
typealias PreviousRateAppRequestTimeInMs = Long

interface PreferencesRepository {

    fun getCurrentAppThemeSynchronously(): AppTheme

    fun getCurrentAppTheme(): Flow<AppTheme>

    fun getCurrentAppLocale(): AppLocale

    fun isPlayInBackgroundEnabled(): Flow<IsBackgroundPlayEnabled>

    fun getNotificationPermissionCount(): Flow<Int>

    fun getAppUpdateDialogShowCount(): Flow<AppUpdateDialogShowCount>

    fun isAppUpdateDialogShownToday(): Flow<IsAppUpdateDialogShownToday>

    fun getSelectedRateAppOption(): Flow<RateAppOption>

    fun getPreviousRateAppRequestTimeInMs(): Flow<PreviousRateAppRequestTimeInMs?>

    suspend fun storeCurrentAppTheme(appTheme: AppTheme)

    suspend fun storeCurrentAppLocale(newAppLocale: AppLocale): IsActivityRestartNeeded

    suspend fun isPlayInBackgroundEnabled(isEnabled: Boolean)

    suspend fun storeNotificationPermissionCount(count: Int)

    suspend fun storeAppUpdateDialogShowCount(count: Int)

    suspend fun storeAppUpdateDialogShowEpochDays()

    suspend fun removeAppUpdateDialogShowEpochDays()

    suspend fun storeSelectedRateAppOption(rateAppOption: RateAppOption)

    suspend fun storePreviousRateAppRequestTimeInMs()
}