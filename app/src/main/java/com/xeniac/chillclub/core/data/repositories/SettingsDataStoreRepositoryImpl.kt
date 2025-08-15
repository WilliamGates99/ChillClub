package com.xeniac.chillclub.core.data.repositories

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import com.xeniac.chillclub.core.domain.models.AppLocale
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.models.IsBackgroundPlayEnabled
import com.xeniac.chillclub.core.domain.models.SettingsPreferences
import com.xeniac.chillclub.core.domain.repositories.IsActivityRestartNeeded
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class SettingsDataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<SettingsPreferences>
) : SettingsDataStoreRepository {

    override fun getCurrentAppThemeSynchronously(): AppTheme = try {
        val appThemeIndex = runBlocking {
            dataStore.data.first().themeIndex
        }

        when (appThemeIndex) {
            AppTheme.Light.index -> AppTheme.Light
            AppTheme.Dark.index -> AppTheme.Dark
            else -> AppTheme.Dark
        }
    } catch (e: Exception) {
        Timber.e("Get current app theme synchronously failed:")
        e.printStackTrace()
        AppTheme.Dark
    }

    override fun getCurrentAppTheme(): Flow<AppTheme> = dataStore.data.map {
        val appThemeIndex = it.themeIndex

        when (appThemeIndex) {
            AppTheme.Light.index -> AppTheme.Light
            AppTheme.Dark.index -> AppTheme.Dark
            else -> AppTheme.Dark
        }
    }.catch { e ->
        Timber.e("Get current app theme failed:")
        e.printStackTrace()
    }

    override suspend fun storeCurrentAppTheme(
        appTheme: AppTheme
    ) {
        try {
            dataStore.updateData { it.copy(themeIndex = appTheme.index) }
            Timber.i("Current app theme edited to ${appTheme.index}")
        } catch (e: Exception) {
            Timber.e("Store current app theme failed:")
            e.printStackTrace()
        }
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
                AppLocale.EnglishUS.localeString -> AppLocale.EnglishUS
                AppLocale.FarsiIR.localeString -> AppLocale.FarsiIR
                else -> AppLocale.Default
            }
        }
    } catch (e: Exception) {
        Timber.e("Get current app locale failed:")
        e.printStackTrace()
        AppLocale.Default
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
        Timber.e("Store current app locale failed:")
        e.printStackTrace()
        false
    }

    private fun isActivityRestartNeeded(
        newLocale: AppLocale
    ): Boolean = getCurrentAppLocale().layoutDirectionCompose != newLocale.layoutDirectionCompose

    override fun isPlayInBackgroundEnabled(): Flow<IsBackgroundPlayEnabled> = dataStore.data.map {
        it.isPlayInBackgroundEnabled
    }.catch { e ->
        Timber.e("Get is play in background enabled failed:")
        e.printStackTrace()
    }

    override suspend fun isPlayInBackgroundEnabled(isEnabled: Boolean) {
        try {
            dataStore.updateData { it.copy(isPlayInBackgroundEnabled = isEnabled) }
            Timber.i("Current is play in background enabled edited to $isEnabled")
        } catch (e: Exception) {
            Timber.e("Store is play in background enabled failed:")
            e.printStackTrace()
        }
    }
}