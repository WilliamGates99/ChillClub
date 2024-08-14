package com.xeniac.chillclub.core.domain.repositories

import com.xeniac.chillclub.core.data.dto.AppLocaleDto
import com.xeniac.chillclub.core.data.dto.AppThemeDto
import com.xeniac.chillclub.core.data.dto.RateAppOptionDto
import com.xeniac.chillclub.core.domain.models.AppLocale
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.models.RateAppOption
import kotlinx.coroutines.flow.Flow

typealias IsBackgroundPlayEnabled = Boolean
typealias IsActivityRestartNeeded = Boolean
typealias PreviousRateAppRequestTimeInMs = Long

interface PreferencesRepository {

    fun getCurrentAppThemeSynchronously(): AppTheme

    fun getCurrentAppTheme(): Flow<AppTheme>

    suspend fun getCurrentAppLocale(): AppLocale

    fun isPlayInBackgroundEnabled(): Flow<IsBackgroundPlayEnabled>

    suspend fun getNotificationPermissionCount(): Int

    suspend fun getSelectedRateAppOption(): Flow<RateAppOption>

    suspend fun getPreviousRateAppRequestTimeInMs(): Flow<PreviousRateAppRequestTimeInMs?>

    suspend fun setCurrentAppTheme(appThemeDto: AppThemeDto)

    suspend fun setCurrentAppLocale(appLocaleDto: AppLocaleDto): IsActivityRestartNeeded

    suspend fun isPlayInBackgroundEnabled(isEnabled: Boolean)

    suspend fun setNotificationPermissionCount(count: Int)

    suspend fun setSelectedRateAppOption(rateAppOptionDto: RateAppOptionDto)

    suspend fun setPreviousRateAppRequestTimeInMs()
}