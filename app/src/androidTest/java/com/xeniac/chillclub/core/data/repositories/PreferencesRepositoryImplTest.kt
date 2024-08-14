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
import com.xeniac.chillclub.core.data.mapper.toAppThemeDto
import com.xeniac.chillclub.core.data.mapper.toRateAppOptionDto
import com.xeniac.chillclub.core.domain.models.AppLocale
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.models.RateAppOption
import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.createTestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("DEPRECATION")
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class PreferencesRepositoryImplTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val testScope: TestCoroutineScope = createTestCoroutineScope(testDispatcher)

    private val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope,
        produceFile = { context.preferencesDataStoreFile(name = "settings_test") }
    )

    private val testRepository: PreferencesRepository = PreferencesRepositoryImpl(
        settingsDataStore = testDataStore
    )

    @Before
    fun setUp() {
        testScope.launch(
            context = testDispatcher
        ) {
            testDataStore.edit { it.clear() }
        }
    }

    @After
    fun tearDown() {
        testScope.cancel()
    }

    /*
    Fetch Initial Preferences Test Cases:
    getCurrentAppThemeSynchronously -> AppTheme.Default
    getCurrentAppTheme -> AppTheme.Default
    getCurrentAppLocale -> AppLocale.Default
    isPlayInBackgroundEnabled -> true
    getNotificationPermissionCount -> 0
    getSelectedRateAppOption -> RateAppOption.NOT_SHOWN_YET
    getPreviousRateAppRequestTimeInMs -> null
     */
    @Test
    fun fetchInitialPreferences() = testScope.runBlockingTest {
        val initialAppThemeSynchronously = testRepository.getCurrentAppThemeSynchronously()
        val initialAppTheme = testRepository.getCurrentAppTheme().first()
        val initialAppLocale = testRepository.getCurrentAppLocale()
        val initialIsPlayInBackgroundEnabled = testRepository.isPlayInBackgroundEnabled().first()
        val initialNotificationPermissionCount = testRepository.getNotificationPermissionCount()
        val initialSelectedRateAppOption = testRepository.getSelectedRateAppOption().first()
        val initialPreviousRateAppRequestTime =
            testRepository.getPreviousRateAppRequestTimeInMs().first()

        assertThat(initialAppThemeSynchronously).isEqualTo(AppTheme.Default)
        assertThat(initialAppTheme).isEqualTo(AppTheme.Default)
        assertThat(initialAppLocale).isEqualTo(AppLocale.Default)
        assertThat(initialIsPlayInBackgroundEnabled).isTrue()
        assertThat(initialNotificationPermissionCount).isEqualTo(0)
        assertThat(initialSelectedRateAppOption).isEqualTo(RateAppOption.NOT_SHOWN_YET)
        assertThat(initialPreviousRateAppRequestTime).isNull()
    }

    @Test
    fun writeCurrentAppTheme() = testScope.runBlockingTest {
        val testValue = AppTheme.Dark
        testRepository.setCurrentAppTheme(testValue.toAppThemeDto())

        val currentAppTheme = testRepository.getCurrentAppTheme().first()
        assertThat(currentAppTheme).isEqualTo(testValue)
    }

    @Test
    fun writeIsPlayInBackgroundEnabled() = testScope.runBlockingTest {
        val testValue = false
        testRepository.isPlayInBackgroundEnabled(testValue)

        val isPlayInBackgroundEnabled = testRepository.isPlayInBackgroundEnabled().first()
        assertThat(isPlayInBackgroundEnabled).isEqualTo(testValue)
    }

    @Test
    fun writeNotificationPermissionCount() = testScope.runBlockingTest {
        val testValue = 2
        testRepository.setNotificationPermissionCount(testValue)

        val notificationPermissionCount = testRepository.getNotificationPermissionCount()
        assertThat(notificationPermissionCount).isEqualTo(testValue)
    }

    @Test
    fun writeSelectedRateAppOption() = testScope.runBlockingTest {
        val testValue = RateAppOption.RATE_NOW
        testRepository.setSelectedRateAppOption(testValue.toRateAppOptionDto())

        val selectedRateAppOption = testRepository.getSelectedRateAppOption().first()
        assertThat(selectedRateAppOption).isEqualTo(testValue)
    }

    @Test
    fun writePreviousRateAppRequestTimeInMs() = testScope.runBlockingTest {
        testRepository.setPreviousRateAppRequestTimeInMs()

        val previousRateAppRequestTime = testRepository.getPreviousRateAppRequestTimeInMs().first()
        assertThat(previousRateAppRequestTime).isNotNull()
    }
}