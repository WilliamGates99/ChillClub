package com.xeniac.chillclub.core.data.repositories

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.xeniac.chillclub.MainCoroutineRule
import com.xeniac.chillclub.core.domain.models.AppLocale
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class SettingsDataStoreRepositoryImplTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val testScope: TestScope = TestScope(context = testDispatcher)

    private val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope.backgroundScope,
        produceFile = { context.preferencesDataStoreFile(name = "Settings-Test") }
    )

    private val testRepository: SettingsDataStoreRepository = SettingsDataStoreRepositoryImpl(
        dataStore = testDataStore
    )

    @Before
    fun setUp() {
        testScope.launch {
            testDataStore.edit { it.clear() }
        }
    }

    @After
    fun tearDown() {
        testScope.cancel()
    }

    /*
  Fetch Initial Preferences Test Cases:
  getCurrentAppThemeSynchronously -> AppTheme.Dark
  getCurrentAppTheme -> AppTheme.Dark
  getCurrentAppLocale -> AppLocale.Default
  isPlayInBackgroundEnabled -> true
  getNotificationPermissionCount -> 0
   */
    @Test
    fun fetchInitialPreferences() = testScope.runTest {
        val initialAppThemeSynchronously = testRepository.getCurrentAppThemeSynchronously()
        val initialAppTheme = testRepository.getCurrentAppTheme().first()
        val initialAppLocale = testRepository.getCurrentAppLocale()
        val initialIsPlayInBackgroundEnabled = testRepository
            .isPlayInBackgroundEnabled().first()
        val initialNotificationPermissionCount = testRepository
            .getNotificationPermissionCount().first()

        assertThat(initialAppThemeSynchronously).isEqualTo(AppTheme.Dark)
        assertThat(initialAppTheme).isEqualTo(AppTheme.Dark)
        assertThat(initialAppLocale).isEqualTo(AppLocale.Default)
        assertThat(initialIsPlayInBackgroundEnabled).isTrue()
        assertThat(initialNotificationPermissionCount).isEqualTo(0)
    }

    @Test
    fun writeCurrentAppTheme() = testScope.runTest {
        val testValue = AppTheme.Light
        testRepository.storeCurrentAppTheme(testValue)

        val currentAppTheme = testRepository.getCurrentAppTheme().first()
        assertThat(currentAppTheme).isEqualTo(testValue)
    }

    @Test
    fun writeIsPlayInBackgroundEnabled() = testScope.runTest {
        val testValue = false
        testRepository.isPlayInBackgroundEnabled(testValue)

        val isPlayInBackgroundEnabled = testRepository.isPlayInBackgroundEnabled().first()
        assertThat(isPlayInBackgroundEnabled).isEqualTo(testValue)
    }

    @Test
    fun writeNotificationPermissionCount() = testScope.runTest {
        val testValue = 2
        testRepository.storeNotificationPermissionCount(testValue)

        val notificationPermissionCount = testRepository.getNotificationPermissionCount().first()
        assertThat(notificationPermissionCount).isEqualTo(testValue)
    }
}