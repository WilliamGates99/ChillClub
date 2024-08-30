package com.xeniac.chillclub.core.data.repositories

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xeniac.chillclub.core.data.utils.DateHelper
import com.xeniac.chillclub.core.domain.models.AppLocale
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.models.RateAppOption
import com.xeniac.chillclub.core.domain.repositories.AppUpdateDialogShowCount
import com.xeniac.chillclub.core.domain.repositories.IsActivityRestartNeeded
import com.xeniac.chillclub.core.domain.repositories.IsAppUpdateDialogShownToday
import com.xeniac.chillclub.core.domain.repositories.IsBackgroundPlayEnabled
import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository
import com.xeniac.chillclub.core.domain.repositories.PreviousRateAppRequestTimeInMs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.periodUntil
import kotlinx.datetime.todayIn
import javax.inject.Inject

class FakePreferencesRepositoryImpl @Inject constructor() : PreferencesRepository {

    var currentAppTheme: AppTheme = AppTheme.Dark
    var currentLocale: AppLocale = AppLocale.Default
    var isPlayInBackgroundEnabled = SnapshotStateList<Boolean>().apply { add(true) }
    var notificationPermissionCount = 0
    var appUpdateDialogShowCount = 0
    var appUpdateDialogShowEpochDays: Int? = null
    var selectedRateAppOption = RateAppOption.NOT_SHOWN_YET
    var previousRateAppRequestTime: PreviousRateAppRequestTimeInMs? = null

    private var shouldStoreTodayDate = true

    fun setShouldStoreTodayDate(value: Boolean) {
        shouldStoreTodayDate = value
    }

    override fun getCurrentAppThemeSynchronously(): AppTheme = currentAppTheme

    override fun getCurrentAppTheme(): Flow<AppTheme> = flow { emit(currentAppTheme) }

    override fun getCurrentAppLocale(): AppLocale = currentLocale

    override fun isPlayInBackgroundEnabled(): Flow<IsBackgroundPlayEnabled> = snapshotFlow {
        isPlayInBackgroundEnabled.first()
    }

    override fun getNotificationPermissionCount(): Flow<Int> = flow {
        emit(notificationPermissionCount)
    }

    override fun getAppUpdateDialogShowCount(): Flow<AppUpdateDialogShowCount> = flow {
        emit(appUpdateDialogShowCount)
    }

    override fun isAppUpdateDialogShownToday(): Flow<IsAppUpdateDialogShownToday> = snapshotFlow {
        val dialogShowEpochDays = appUpdateDialogShowEpochDays

        dialogShowEpochDays?.let { epochDays ->
            val todayDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val dialogShowLocalDate = LocalDate.fromEpochDays(epochDays)

            val isShownToday = dialogShowLocalDate.periodUntil(todayDate).days == 0

            isShownToday
        } ?: false
    }

    override fun getSelectedRateAppOption(): Flow<RateAppOption> = flow {
        emit(selectedRateAppOption)
    }

    override fun getPreviousRateAppRequestTimeInMs(): Flow<PreviousRateAppRequestTimeInMs?> = flow {
        emit(previousRateAppRequestTime)
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

    override suspend fun storeAppUpdateDialogShowCount(count: Int) {
        appUpdateDialogShowCount = count
    }

    override suspend fun storeAppUpdateDialogShowEpochDays() {
        val todayLocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

        val localDate = if (shouldStoreTodayDate) {
            todayLocalDate
        } else {
            todayLocalDate.minus(
                value = 1,
                unit = DateTimeUnit.DAY
            )
        }

        appUpdateDialogShowEpochDays = localDate.toEpochDays()
    }

    override suspend fun removeAppUpdateDialogShowEpochDays() {
        appUpdateDialogShowEpochDays = null
    }

    override suspend fun storeSelectedRateAppOption(rateAppOption: RateAppOption) {
        selectedRateAppOption = rateAppOption
    }

    override suspend fun storePreviousRateAppRequestTimeInMs() {
        previousRateAppRequestTime = DateHelper.getCurrentTimeInMs()
    }

    fun isActivityRestartNeeded(
        newLocale: AppLocale
    ): Boolean = currentLocale.layoutDirectionCompose != newLocale.layoutDirectionCompose
}