package com.xeniac.chillclub.core.data.repositories

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.text.layoutDirection
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
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
import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository
import com.xeniac.chillclub.core.domain.repositories.PreviousRateAppRequestTimeInMs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val settingsDataStore: DataStore<Preferences>
) : PreferencesRepository {

    private object PreferencesKeys {
        val CURRENT_APP_THEME = intPreferencesKey(name = "theme")
        val NOTIFICATION_PERMISSION_COUNT = intPreferencesKey(name = "notificationPermissionCount")
        val SELECTED_RATE_APP_OPTION = stringPreferencesKey(name = "selectedRateAppOption")
        val PREVIOUS_RATE_APP_REQUEST_TIME_IN_MS = longPreferencesKey(
            name = "previousRateAppRequestTimeInMs"
        )
    }

    override fun getCurrentAppThemeSynchronously(): AppTheme = runBlocking {
        try {
            val appThemeIndex = settingsDataStore.data.first()[PreferencesKeys.CURRENT_APP_THEME]

            val appThemeDto = when (appThemeIndex) {
                AppThemeDto.Default.index -> AppThemeDto.Default
                AppThemeDto.Light.index -> AppThemeDto.Light
                AppThemeDto.Dark.index -> AppThemeDto.Dark
                else -> AppThemeDto.Default
            }

            appThemeDto.toAppTheme()
        } catch (e: Exception) {
            Timber.e("getCurrentAppThemeSynchronously failed:")
            e.printStackTrace()
            AppThemeDto.Default.toAppTheme()
        }
    }

    override fun getCurrentAppTheme(): Flow<AppTheme> = settingsDataStore.data.map {
        val appThemeIndex = it[PreferencesKeys.CURRENT_APP_THEME]

        val appThemeDto = when (appThemeIndex) {
            AppThemeDto.Default.index -> AppThemeDto.Default
            AppThemeDto.Light.index -> AppThemeDto.Light
            AppThemeDto.Dark.index -> AppThemeDto.Dark
            else -> AppThemeDto.Default
        }

        appThemeDto.toAppTheme()
    }.catch { e ->
        Timber.e("getCurrentAppTheme failed:")
        e.printStackTrace()
        AppThemeDto.Default.toAppTheme()
    }

    override suspend fun getCurrentAppLocale(): AppLocale = try {
        val appLocaleList = AppCompatDelegate.getApplicationLocales()

        if (appLocaleList.isEmpty) {
            Timber.i("App locale list is Empty.")
            AppLocaleDto.Default.toAppLocale()
        } else {
            val localeString = appLocaleList[0].toString()
            Timber.i("Current app locale string is $localeString")

            val appLocaleDto = when (localeString) {
                AppLocaleDto.Default.localeString -> AppLocaleDto.Default
                AppLocaleDto.EnglishUS.localeString -> AppLocaleDto.EnglishUS
                AppLocaleDto.EnglishGB.localeString -> AppLocaleDto.EnglishGB
                AppLocaleDto.FarsiIR.localeString -> AppLocaleDto.FarsiIR
                else -> AppLocaleDto.Default
            }

            appLocaleDto.toAppLocale()
        }
    } catch (e: Exception) {
        Timber.e("getCurrentAppLocale failed:")
        e.printStackTrace()
        AppLocaleDto.Default.toAppLocale()
    }

    override suspend fun getNotificationPermissionCount(): Int = try {
        settingsDataStore.data.first()[PreferencesKeys.NOTIFICATION_PERMISSION_COUNT] ?: 0
    } catch (e: Exception) {
        Timber.e("getNotificationPermissionCount failed:")
        e.printStackTrace()
        0
    }

    override suspend fun getSelectedRateAppOption(): Flow<RateAppOption> = settingsDataStore.data
        .map {
            val selectedRateAppOption = it[PreferencesKeys.SELECTED_RATE_APP_OPTION]

            val rateAppOptionDto = RateAppOptionDto.entries.find { rateAppOptionDto ->
                rateAppOptionDto.value == selectedRateAppOption
            } ?: RateAppOptionDto.NOT_SHOWN_YET

            rateAppOptionDto.toRateAppOption()
        }.catch { e ->
            Timber.e("getSelectedRateAppOption failed:")
            e.printStackTrace()
            RateAppOptionDto.NOT_SHOWN_YET.toRateAppOption()
        }

    override suspend fun getPreviousRateAppRequestTimeInMs(): Flow<PreviousRateAppRequestTimeInMs?> =
        settingsDataStore.data.map {
            it[PreferencesKeys.PREVIOUS_RATE_APP_REQUEST_TIME_IN_MS]
        }.catch { e ->
            Timber.e("getPreviousRateAppRequestTimeInMs failed:")
            e.printStackTrace()
            emit(null)
        }

    override suspend fun setCurrentAppTheme(appThemeDto: AppThemeDto) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.CURRENT_APP_THEME] = appThemeDto.index
                Timber.i("AppTheme edited to ${appThemeDto.index}")
            }
        } catch (e: Exception) {
            Timber.e("setCurrentAppTheme failed:")
            e.printStackTrace()
        }
    }

    override suspend fun setCurrentAppLocale(
        appLocaleDto: AppLocaleDto
    ): IsActivityRestartNeeded = try {
        val isActivityRestartNeeded = isActivityRestartNeeded(
            newLayoutDirection = appLocaleDto.layoutDirection
        )
        AppCompatDelegate.setApplicationLocales(
            /* locales = */ LocaleListCompat.forLanguageTags(appLocaleDto.languageTag)
        )
        isActivityRestartNeeded
    } catch (e: Exception) {
        Timber.e("setCurrentAppLocale failed:")
        e.printStackTrace()
        false
    }

    override suspend fun setNotificationPermissionCount(count: Int) {
        try {
            settingsDataStore.edit { preferences ->
                preferences[PreferencesKeys.NOTIFICATION_PERMISSION_COUNT] = count
                Timber.i("Notification permission count edited to $count")
            }
        } catch (e: Exception) {
            Timber.e("setNotificationPermissionCount failed:")
            e.printStackTrace()
        }
    }

    override suspend fun setSelectedRateAppOption(rateAppOptionDto: RateAppOptionDto) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.SELECTED_RATE_APP_OPTION] = rateAppOptionDto.value
                Timber.i("setSelectedRateAppOption edited to ${rateAppOptionDto.value}")
            }
        } catch (e: Exception) {
            Timber.e("setSelectedRateAppOption failed:")
            e.printStackTrace()
        }
    }

    override suspend fun setPreviousRateAppRequestTimeInMs() {
        try {
            val currentTimeInMs = DateHelper.getCurrentTimeInMillis()
            settingsDataStore.edit {
                it[PreferencesKeys.PREVIOUS_RATE_APP_REQUEST_TIME_IN_MS] = currentTimeInMs
                Timber.i("setPreviousRateAppRequestTimeInMs edited to $currentTimeInMs")
            }
        } catch (e: Exception) {
            Timber.e("setPreviousRateAppRequestTimeInMs failed:")
            e.printStackTrace()
        }
    }

    private fun isActivityRestartNeeded(newLayoutDirection: Int): Boolean {
        val currentLocale = AppCompatDelegate.getApplicationLocales()[0]
        val currentLayoutDirection = currentLocale?.layoutDirection
        return currentLayoutDirection != newLayoutDirection
    }
}