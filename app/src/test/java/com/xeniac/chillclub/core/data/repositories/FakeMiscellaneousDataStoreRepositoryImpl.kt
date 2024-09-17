package com.xeniac.chillclub.core.data.repositories

import androidx.compose.runtime.snapshotFlow
import com.xeniac.chillclub.core.data.utils.DateHelper
import com.xeniac.chillclub.core.domain.models.RateAppOption
import com.xeniac.chillclub.core.domain.repositories.AppUpdateDialogShowCount
import com.xeniac.chillclub.core.domain.repositories.IsAppUpdateDialogShownToday
import com.xeniac.chillclub.core.domain.repositories.MiscellaneousDataStoreRepository
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

class FakeMiscellaneousDataStoreRepositoryImpl : MiscellaneousDataStoreRepository {

    var appUpdateDialogShowCount = 0
    var appUpdateDialogShowEpochDays: Int? = null
    var selectedRateAppOption = RateAppOption.NOT_SHOWN_YET
    var previousRateAppRequestTime: PreviousRateAppRequestTimeInMs? = null

    private var shouldStoreTodayDate = true

    fun setShouldStoreTodayDate(value: Boolean) {
        shouldStoreTodayDate = value
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
}