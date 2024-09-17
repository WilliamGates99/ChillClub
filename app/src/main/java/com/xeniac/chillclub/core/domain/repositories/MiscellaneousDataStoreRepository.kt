package com.xeniac.chillclub.core.domain.repositories

import com.xeniac.chillclub.core.domain.models.RateAppOption
import kotlinx.coroutines.flow.Flow

typealias AppUpdateDialogShowCount = Int
typealias IsAppUpdateDialogShownToday = Boolean
typealias PreviousRateAppRequestTimeInMs = Long

interface MiscellaneousDataStoreRepository {

    fun getAppUpdateDialogShowCount(): Flow<AppUpdateDialogShowCount>

    fun isAppUpdateDialogShownToday(): Flow<IsAppUpdateDialogShownToday>

    fun getSelectedRateAppOption(): Flow<RateAppOption>

    fun getPreviousRateAppRequestTimeInMs(): Flow<PreviousRateAppRequestTimeInMs?>

    suspend fun storeAppUpdateDialogShowCount(count: Int)

    suspend fun storeAppUpdateDialogShowEpochDays()

    suspend fun removeAppUpdateDialogShowEpochDays()

    suspend fun storeSelectedRateAppOption(rateAppOption: RateAppOption)

    suspend fun storePreviousRateAppRequestTimeInMs()
}