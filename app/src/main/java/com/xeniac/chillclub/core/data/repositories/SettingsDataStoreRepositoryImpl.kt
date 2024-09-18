package com.xeniac.chillclub.core.data.repositories

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.xeniac.chillclub.core.di.SettingsDataStoreQualifier
import com.xeniac.chillclub.core.domain.models.AppLocale
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.repositories.IsActivityRestartNeeded
import com.xeniac.chillclub.core.domain.repositories.IsBackgroundPlayEnabled
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class SettingsDataStoreRepositoryImpl @Inject constructor(
    @SettingsDataStoreQualifier private val dataStore: DataStore<Preferences>
) : SettingsDataStoreRepository {

    private object PreferencesKeys {
        val IS_DARK_THEME_ENABLED = booleanPreferencesKey(name = "isDarkThemeEnabled")
        val IS_PLAY_IN_BACKGROUND_ENABLED = booleanPreferencesKey(
            name = "isPlayInBackgroundEnabled"
        )
        val NOTIFICATION_PERMISSION_COUNT = intPreferencesKey(name = "notificationPermissionCount")
    }

    override fun getCurrentAppThemeSynchronously(): AppTheme = runBlocking {
        try {
            val isDarkThemeEnabled = dataStore.data
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

    override fun getCurrentAppTheme(): Flow<AppTheme> = dataStore.data.map {
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

    override fun isPlayInBackgroundEnabled(): Flow<IsBackgroundPlayEnabled> = dataStore.data.map {
        it[PreferencesKeys.IS_PLAY_IN_BACKGROUND_ENABLED] ?: true
    }.catch { e ->
        Timber.e("get isPlayInBackgroundEnabled failed:")
        e.printStackTrace()
    }

    override fun getNotificationPermissionCount(): Flow<Int> = dataStore.data.map {
        it[PreferencesKeys.NOTIFICATION_PERMISSION_COUNT] ?: 0
    }.catch { e ->
        Timber.e("getNotificationPermissionCount failed:")
        e.printStackTrace()
    }

    override suspend fun storeCurrentAppTheme(appTheme: AppTheme) {
        try {
            dataStore.edit {
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
            dataStore.edit {
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
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.NOTIFICATION_PERMISSION_COUNT] = count
                Timber.i("Notification permission count edited to $count")
            }
        } catch (e: Exception) {
            Timber.e("storeNotificationPermissionCount failed:")
            e.printStackTrace()
        }
    }

    private fun isActivityRestartNeeded(
        newLocale: AppLocale
    ): Boolean = getCurrentAppLocale().layoutDirectionCompose != newLocale.layoutDirectionCompose
}