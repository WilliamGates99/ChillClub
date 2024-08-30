package com.xeniac.chillclub.core.data.repositories

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlinx.datetime.todayIn
import timber.log.Timber
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val settingsDataStore: DataStore<Preferences>
) : PreferencesRepository {

    private object PreferencesKeys {
        val IS_DARK_THEME_ENABLED = booleanPreferencesKey(name = "isDarkThemeEnabled")
        val IS_PLAY_IN_BACKGROUND_ENABLED = booleanPreferencesKey(
            name = "isPlayInBackgroundEnabled"
        )
        val NOTIFICATION_PERMISSION_COUNT = intPreferencesKey(name = "notificationPermissionCount")
        val APP_UPDATE_DIALOG_SHOW_COUNT = intPreferencesKey(name = "AppUpdateDialogShowCount")
        val APP_UPDATE_DIALOG_SHOW_EPOCH_DAYS = intPreferencesKey(
            name = "appUpdateDialogShowEpochDays"
        )
        val SELECTED_RATE_APP_OPTION = stringPreferencesKey(name = "selectedRateAppOption")
        val PREVIOUS_RATE_APP_REQUEST_TIME_IN_MS = longPreferencesKey(
            name = "previousRateAppRequestTimeInMs"
        )
    }

    override fun getCurrentAppThemeSynchronously(): AppTheme = runBlocking {
        try {
            val isDarkThemeEnabled = settingsDataStore.data
                .first()[PreferencesKeys.IS_DARK_THEME_ENABLED]

            when (isDarkThemeEnabled) {
                true -> AppTheme.Dark
                false -> AppTheme.Light
                null -> AppTheme.Dark
            }
        } catch (e: Exception) {
            Timber.e("getCurrentAppThemeSynchronously failed:")
            e.printStackTrace()
            AppTheme.Dark
        }
    }

    override fun getCurrentAppTheme(): Flow<AppTheme> = settingsDataStore.data.map {
        val isDarkThemeEnabled = it[PreferencesKeys.IS_DARK_THEME_ENABLED]

        when (isDarkThemeEnabled) {
            true -> AppTheme.Dark
            false -> AppTheme.Light
            null -> AppTheme.Dark
        }
    }.catch { e ->
        Timber.e("getCurrentAppTheme failed:")
        e.printStackTrace()
    }

    override fun getCurrentAppLocale(): AppLocale = try {
        val appLocaleList = AppCompatDelegate.getApplicationLocales()

        if (appLocaleList.isEmpty) {
            Timber.i("App locale list is Empty.")
            AppLocale.Default
        } else {
            val localeString = appLocaleList[0].toString()
            Timber.i("Current app locale string is $localeString")

            when (localeString) {
                AppLocale.Default.localeString -> AppLocale.Default
                AppLocale.EnglishUS.localeString -> AppLocale.EnglishUS
                AppLocale.EnglishGB.localeString -> AppLocale.EnglishGB
                AppLocale.FarsiIR.localeString -> AppLocale.FarsiIR
                else -> AppLocale.Default
            }
        }
    } catch (e: Exception) {
        Timber.e("getCurrentAppLocale failed:")
        e.printStackTrace()
        AppLocale.Default
    }

    override fun isPlayInBackgroundEnabled(): Flow<IsBackgroundPlayEnabled> = settingsDataStore.data
        .map {
            it[PreferencesKeys.IS_PLAY_IN_BACKGROUND_ENABLED] ?: true
        }.catch { e ->
            Timber.e("get isPlayInBackgroundEnabled failed:")
            e.printStackTrace()
        }

    override fun getNotificationPermissionCount(): Flow<Int> = settingsDataStore.data.map {
        it[PreferencesKeys.NOTIFICATION_PERMISSION_COUNT] ?: 0
    }.catch { e ->
        Timber.e("getNotificationPermissionCount failed:")
        e.printStackTrace()
    }

    override fun getAppUpdateDialogShowCount(): Flow<AppUpdateDialogShowCount> =
        settingsDataStore.data.map {
            it[PreferencesKeys.APP_UPDATE_DIALOG_SHOW_COUNT] ?: 0
        }.catch { e ->
            Timber.e("getAppUpdateDialogShowCount failed:")
            e.printStackTrace()
        }

    override fun isAppUpdateDialogShownToday(): Flow<IsAppUpdateDialogShownToday> =
        settingsDataStore.data.map {
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

    override fun getSelectedRateAppOption(): Flow<RateAppOption> = settingsDataStore.data
        .map {
            val selectedRateAppOption = it[PreferencesKeys.SELECTED_RATE_APP_OPTION]

            RateAppOption.entries.find { rateAppOption ->
                rateAppOption.value == selectedRateAppOption
            } ?: RateAppOption.NOT_SHOWN_YET
        }.catch { e ->
            Timber.e("getSelectedRateAppOption failed:")
            e.printStackTrace()
        }

    override fun getPreviousRateAppRequestTimeInMs(): Flow<PreviousRateAppRequestTimeInMs?> =
        settingsDataStore.data.map {
            it[PreferencesKeys.PREVIOUS_RATE_APP_REQUEST_TIME_IN_MS]
        }.catch { e ->
            Timber.e("getPreviousRateAppRequestTimeInMs failed:")
            e.printStackTrace()
        }

    override suspend fun storeCurrentAppTheme(appTheme: AppTheme) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.IS_DARK_THEME_ENABLED] = when (appTheme) {
                    AppTheme.Light -> false
                    AppTheme.Dark -> true
                }
                Timber.i("AppTheme edited to $appTheme")
            }
        } catch (e: Exception) {
            Timber.e("storeCurrentAppTheme failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storeCurrentAppLocale(
        newAppLocale: AppLocale
    ): IsActivityRestartNeeded = try {
        val isActivityRestartNeeded = isActivityRestartNeeded(newAppLocale)

        AppCompatDelegate.setApplicationLocales(
            /* locales = */ LocaleListCompat.forLanguageTags(newAppLocale.languageTag)
        )

        isActivityRestartNeeded
    } catch (e: Exception) {
        Timber.e("storeCurrentAppLocale failed:")
        e.printStackTrace()
        false
    }

    override suspend fun isPlayInBackgroundEnabled(isEnabled: Boolean) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.IS_PLAY_IN_BACKGROUND_ENABLED] = isEnabled
                Timber.i("isPlayInBackgroundEnabled edited to $isEnabled")
            }
        } catch (e: Exception) {
            Timber.e("set isPlayInBackgroundEnabled failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storeNotificationPermissionCount(count: Int) {
        try {
            settingsDataStore.edit { preferences ->
                preferences[PreferencesKeys.NOTIFICATION_PERMISSION_COUNT] = count
                Timber.i("Notification permission count edited to $count")
            }
        } catch (e: Exception) {
            Timber.e("storeNotificationPermissionCount failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storeAppUpdateDialogShowCount(count: Int) {
        try {
            settingsDataStore.edit {
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

            settingsDataStore.edit {
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
            settingsDataStore.edit {
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
            settingsDataStore.edit {
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
            settingsDataStore.edit {
                it[PreferencesKeys.PREVIOUS_RATE_APP_REQUEST_TIME_IN_MS] = currentTimeInMs
                Timber.i("Previous rate app request time edited to $currentTimeInMs")
            }
        } catch (e: Exception) {
            Timber.e("storePreviousRateAppRequestTimeInMs failed:")
            e.printStackTrace()
        }
    }

    private fun isActivityRestartNeeded(
        newLocale: AppLocale
    ): Boolean = getCurrentAppLocale().layoutDirectionCompose != newLocale.layoutDirectionCompose
}