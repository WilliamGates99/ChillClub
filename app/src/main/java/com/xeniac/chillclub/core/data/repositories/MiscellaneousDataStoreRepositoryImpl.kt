package com.xeniac.chillclub.core.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.xeniac.chillclub.core.data.utils.DateHelper
import com.xeniac.chillclub.core.di.MiscellaneousDataStoreQualifier
import com.xeniac.chillclub.core.domain.models.RateAppOption
import com.xeniac.chillclub.core.domain.repositories.AppUpdateDialogShowCount
import com.xeniac.chillclub.core.domain.repositories.IsAppUpdateDialogShownToday
import com.xeniac.chillclub.core.domain.repositories.MiscellaneousDataStoreRepository
import com.xeniac.chillclub.core.domain.repositories.PreviousRateAppRequestTimeInMs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlinx.datetime.todayIn
import timber.log.Timber
import javax.inject.Inject

class MiscellaneousDataStoreRepositoryImpl @Inject constructor(
    @MiscellaneousDataStoreQualifier private val dataStore: DataStore<Preferences>
) : MiscellaneousDataStoreRepository {

    private object PreferencesKeys {
        val APP_UPDATE_DIALOG_SHOW_COUNT = intPreferencesKey(name = "AppUpdateDialogShowCount")
        val APP_UPDATE_DIALOG_SHOW_EPOCH_DAYS = intPreferencesKey(
            name = "appUpdateDialogShowEpochDays"
        )
        val SELECTED_RATE_APP_OPTION = stringPreferencesKey(name = "selectedRateAppOption")
        val PREVIOUS_RATE_APP_REQUEST_TIME_IN_MS = longPreferencesKey(
            name = "previousRateAppRequestTimeInMs"
        )
    }

    override fun getAppUpdateDialogShowCount(): Flow<AppUpdateDialogShowCount> =
        dataStore.data.map {
            it[PreferencesKeys.APP_UPDATE_DIALOG_SHOW_COUNT] ?: 0
        }.catch { e ->
            Timber.e("getAppUpdateDialogShowCount failed:")
            e.printStackTrace()
        }

    override fun isAppUpdateDialogShownToday(): Flow<IsAppUpdateDialogShownToday> =
        dataStore.data.map {
            val dialogShowEpochDays = it[PreferencesKeys.APP_UPDATE_DIALOG_SHOW_EPOCH_DAYS]

            dialogShowEpochDays?.let { epochDays ->
                val todayDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
                val dialogShowLocalDate = LocalDate.fromEpochDays(epochDays)

                val isShownToday = dialogShowLocalDate.periodUntil(todayDate).days == 0

                isShownToday
            } ?: false
        }.catch { e ->
            Timber.e("isAppUpdateDialogShownToday failed:")
            e.printStackTrace()
        }

    override fun getSelectedRateAppOption(): Flow<RateAppOption> = dataStore.data.map {
        val selectedRateAppOption = it[PreferencesKeys.SELECTED_RATE_APP_OPTION]

        RateAppOption.entries.find { rateAppOption ->
            rateAppOption.value == selectedRateAppOption
        } ?: RateAppOption.NOT_SHOWN_YET
    }.catch { e ->
        Timber.e("getSelectedRateAppOption failed:")
        e.printStackTrace()
    }

    override fun getPreviousRateAppRequestTimeInMs(): Flow<PreviousRateAppRequestTimeInMs?> =
        dataStore.data.map {
            it[PreferencesKeys.PREVIOUS_RATE_APP_REQUEST_TIME_IN_MS]
        }.catch { e ->
            Timber.e("getPreviousRateAppRequestTimeInMs failed:")
            e.printStackTrace()
        }

    override suspend fun storeAppUpdateDialogShowCount(count: Int) {
        try {
            dataStore.edit {
                it[PreferencesKeys.APP_UPDATE_DIALOG_SHOW_COUNT] = count
                Timber.i("App update dialog show count edited to $count")
            }
        } catch (e: Exception) {
            Timber.e("storeAppUpdateDialogShowCount failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storeAppUpdateDialogShowEpochDays() {
        try {
            val todayLocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val todayEpochDays = todayLocalDate.toEpochDays()

            dataStore.edit {
                it[PreferencesKeys.APP_UPDATE_DIALOG_SHOW_EPOCH_DAYS] = todayEpochDays
                Timber.i("App update dialog show epoch days edited to $todayEpochDays")
            }
        } catch (e: Exception) {
            Timber.e("storeAppUpdateDialogShowEpochDays failed:")
            e.printStackTrace()
        }
    }

    override suspend fun removeAppUpdateDialogShowEpochDays() {
        try {
            dataStore.edit {
                it.remove(PreferencesKeys.APP_UPDATE_DIALOG_SHOW_EPOCH_DAYS)
                Timber.i("App update dialog show epoch days removed")
            }
        } catch (e: Exception) {
            Timber.e("removeAppUpdateDialogShowEpochDays failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storeSelectedRateAppOption(rateAppOption: RateAppOption) {
        try {
            dataStore.edit {
                it[PreferencesKeys.SELECTED_RATE_APP_OPTION] = rateAppOption.value
                Timber.i("setSelectedRateAppOption edited to ${rateAppOption.value}")
            }
        } catch (e: Exception) {
            Timber.e("storeSelectedRateAppOption failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storePreviousRateAppRequestTimeInMs() {
        try {
            val currentTimeInMs = DateHelper.getCurrentTimeInMs()
            dataStore.edit {
                it[PreferencesKeys.PREVIOUS_RATE_APP_REQUEST_TIME_IN_MS] = currentTimeInMs
                Timber.i("Previous rate app request time edited to $currentTimeInMs")
            }
        } catch (e: Exception) {
            Timber.e("storePreviousRateAppRequestTimeInMs failed:")
            e.printStackTrace()
        }
    }
}