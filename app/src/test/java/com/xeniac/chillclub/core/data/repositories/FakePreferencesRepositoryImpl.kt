package com.xeniac.chillclub.core.data.repositories

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xeniac.chillclub.core.data.dto.AppLocaleDto
import com.xeniac.chillclub.core.data.dto.AppThemeDto
import com.xeniac.chillclub.core.data.dto.RateAppOptionDto
import com.xeniac.chillclub.core.data.mapper.toAppLocale
import com.xeniac.chillclub.core.data.mapper.toAppTheme
import com.xeniac.chillclub.core.data.mapper.toRateAppOption
import com.xeniac.chillclub.core.data.utils.DateHelper
import com.xeniac.chillclub.core.domain.models.AppLocale
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.models.RateAppOption
import com.xeniac.chillclub.core.domain.repositories.IsActivityRestartNeeded
import com.xeniac.chillclub.core.domain.repositories.IsBackgroundPlayEnabled
import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository
import com.xeniac.chillclub.core.domain.repositories.PreviousRateAppRequestTimeInMs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakePreferencesRepositoryImpl @Inject constructor() : PreferencesRepository {

    var appTheme: AppTheme = AppTheme.Dark
    var appLocale: AppLocale = AppLocale.Default
    var isPlayInBackgroundEnabled = SnapshotStateList<Boolean>().apply {
        add(true)
    }
    var notificationPermissionCount = 0
    var selectedRateAppOption: RateAppOption = RateAppOption.NOT_SHOWN_YET
    var previousRateAppRequestTime: PreviousRateAppRequestTimeInMs? = null

    override fun getCurrentAppThemeSynchronously(): AppTheme = appTheme

    override fun getCurrentAppTheme(): Flow<AppTheme> = flow { emit(appTheme) }

    override suspend fun getCurrentAppLocale(): AppLocale = appLocale

    override fun isPlayInBackgroundEnabled(): Flow<IsBackgroundPlayEnabled> = snapshotFlow {
        isPlayInBackgroundEnabled.first()
    }

    override suspend fun getNotificationPermissionCount(): Int = notificationPermissionCount

    override suspend fun getSelectedRateAppOption(): Flow<RateAppOption> = flow {
        emit(selectedRateAppOption)
    }

    override suspend fun getPreviousRateAppRequestTimeInMs(): Flow<PreviousRateAppRequestTimeInMs?> =
        flow { previousRateAppRequestTime }

    override suspend fun setCurrentAppTheme(appThemeDto: AppThemeDto) {
        appTheme = appThemeDto.toAppTheme()
    }

    override suspend fun setCurrentAppLocale(
        appLocaleDto: AppLocaleDto
    ): IsActivityRestartNeeded {
        val isActivityRestartNeeded = isActivityRestartNeeded(
            newLayoutDirection = appLocaleDto.layoutDirection
        )

        appLocale = appLocaleDto.toAppLocale()

        return isActivityRestartNeeded
    }

    override suspend fun isPlayInBackgroundEnabled(isEnabled: Boolean) {
        isPlayInBackgroundEnabled.apply {
            clear()
            add(isEnabled)
        }
    }

    override suspend fun setNotificationPermissionCount(count: Int) {
        notificationPermissionCount = count
    }

    override suspend fun setSelectedRateAppOption(rateAppOptionDto: RateAppOptionDto) {
        selectedRateAppOption = rateAppOptionDto.toRateAppOption()
    }

    override suspend fun setPreviousRateAppRequestTimeInMs() {
        previousRateAppRequestTime = DateHelper.getCurrentTimeInMillis()
    }

    private fun isActivityRestartNeeded(
        newLayoutDirection: Int
    ): Boolean = appLocale.layoutDirection != newLayoutDirection
}