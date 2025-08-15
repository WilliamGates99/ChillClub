package com.xeniac.chillclub.core.data.repositories

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xeniac.chillclub.core.domain.models.AppUpdateDialogShowCount
import com.xeniac.chillclub.core.domain.models.AppUpdateDialogShowDateTime
import com.xeniac.chillclub.core.domain.models.PreviousRateAppRequestDateTime
import com.xeniac.chillclub.core.domain.models.RateAppOption
import com.xeniac.chillclub.core.domain.repositories.IsAppUpdateDialogShownToday
import com.xeniac.chillclub.core.domain.repositories.MiscellaneousDataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.parse
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toDuration

@OptIn(ExperimentalTime::class)
class FakeMiscellaneousDataStoreRepositoryImpl @Inject constructor(
) : MiscellaneousDataStoreRepository {

    var appUpdateDialogShowCount = SnapshotStateList<AppUpdateDialogShowCount>().apply { add(0) }
    var appUpdateDialogShowDateTime = SnapshotStateList<AppUpdateDialogShowDateTime?>().apply {
        add(null)
    }
    var selectedRateAppOption = SnapshotStateList<RateAppOption>().apply {
        add(RateAppOption.NOT_SHOWN_YET)
    }
    var previousRateAppRequestDateTime =
        SnapshotStateList<PreviousRateAppRequestDateTime?>().apply { add(null) }

    private var shouldStoreTodayDateTime = true

    fun setShouldStoreTodayDateTime(value: Boolean) {
        shouldStoreTodayDateTime = value
    }


    override fun getAppUpdateDialogShowCount(): Flow<AppUpdateDialogShowCount> = snapshotFlow {
        appUpdateDialogShowCount.first()
    }

    override suspend fun storeAppUpdateDialogShowCount(count: Int) {
        appUpdateDialogShowCount.apply {
            clear()
            add(count)
        }
    }

    override fun isAppUpdateDialogShownToday(
        timeZone: TimeZone,
        dateTimeFormat: DateTimeFormat<DateTimeComponents>
    ): Flow<IsAppUpdateDialogShownToday> = snapshotFlow {
        appUpdateDialogShowDateTime.first()?.let { dialogShowDateTime ->
            val today = kotlin.time.Clock.System.now().toLocalDateTime(
                timeZone = timeZone
            ).date

            val dialogShowLocaleDate = Instant.parse(
                input = dialogShowDateTime,
                format = dateTimeFormat
            ).toLocalDateTime(timeZone = timeZone).date

            val isShownToday = today == dialogShowLocaleDate

            isShownToday
        } ?: false
    }

    override suspend fun storeAppUpdateDialogShowDateTime(
        dateTimeFormat: DateTimeFormat<DateTimeComponents>
    ) {
        val dateTimeInstant = if (shouldStoreTodayDateTime) {
            kotlin.time.Clock.System.now()
        } else {
            val now = kotlin.time.Clock.System.now()
            now.minus(duration = 1.toDuration(DurationUnit.DAYS))
        }

        appUpdateDialogShowDateTime.apply {
            clear()
            add(dateTimeInstant.format(format = dateTimeFormat))
        }
    }

    override suspend fun removeAppUpdateDialogShowDateTime() {
        appUpdateDialogShowDateTime.apply {
            clear()
            add(null)
        }
    }

    override fun getSelectedRateAppOption(): Flow<RateAppOption> = snapshotFlow {
        selectedRateAppOption.first()
    }

    override suspend fun storeSelectedRateAppOption(rateAppOption: RateAppOption) {
        selectedRateAppOption.apply {
            clear()
            add(rateAppOption)
        }
    }

    override fun getPreviousRateAppRequestDateTime(
    ): Flow<PreviousRateAppRequestDateTime?> = snapshotFlow {
        previousRateAppRequestDateTime.first()
    }

    override suspend fun storePreviousRateAppRequestDateTime(
        dateTimeFormat: DateTimeFormat<DateTimeComponents>
    ) {
        val now = kotlin.time.Clock.System.now()
        previousRateAppRequestDateTime.apply {
            clear()
            add(now.format(format = dateTimeFormat))
        }
    }
}