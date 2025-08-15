package com.xeniac.chillclub.core.data.repositories

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.xeniac.chillclub.MainCoroutineRule
import com.xeniac.chillclub.core.domain.models.MusicPlayerPreferences
import com.xeniac.chillclub.core.domain.models.MusicPlayerPreferencesSerializer
import com.xeniac.chillclub.core.domain.repositories.MusicPlayerDataStoreRepository
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
class MusicPlayerDataStoreRepositoryImplTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val testScope: TestScope = TestScope(context = testDispatcher)

    private val testDataStore: DataStore<MusicPlayerPreferences> = DataStoreFactory.create(
        serializer = MusicPlayerPreferencesSerializer,
        corruptionHandler = ReplaceFileCorruptionHandler { MusicPlayerPreferences() },
        scope = testScope.backgroundScope,
        produceFile = { context.preferencesDataStoreFile(name = "MusicPlayer-Test.pb") }
    )

    private val testRepository: MusicPlayerDataStoreRepository = MusicPlayerDataStoreRepositoryImpl(
        dataStore = testDataStore
    )

    @Before
    fun setUp() {
        testScope.launch {
            testDataStore.updateData { MusicPlayerPreferences() }
        }
    }

    @After
    fun tearDown() {
        testScope.cancel()
    }

    /*
    Fetch Initial Preferences Test Cases:
    getCurrentlyPlayingRadioStationId -> null
     */
    @Test
    fun fetchInitialPreferences() = testScope.runTest {
        val initialCurrentlyPlayingRadioStationId = testRepository
            .getCurrentlyPlayingRadioStationId().first()

        assertThat(initialCurrentlyPlayingRadioStationId).isNull()
    }

    @Test
    fun writeCurrentlyPlayingRadioStationId() = testScope.runTest {
        val testValue = 5L
        testRepository.storeCurrentlyPlayingRadioStationId(testValue)

        val notificationPermissionCount = testRepository.getCurrentlyPlayingRadioStationId().first()
        assertThat(notificationPermissionCount).isEqualTo(testValue)
    }

    @Test
    fun removeCurrentlyPlayingRadioStationId() = testScope.runTest {
        val testValue = 5L
        testRepository.storeCurrentlyPlayingRadioStationId(testValue)

        val notificationPermissionCountBefore = testRepository
            .getCurrentlyPlayingRadioStationId().first()
        assertThat(notificationPermissionCountBefore).isEqualTo(testValue)

        testRepository.removeCurrentlyPlayingRadioStationId()

        val notificationPermissionCountAfter = testRepository
            .getCurrentlyPlayingRadioStationId().first()
        assertThat(notificationPermissionCountAfter).isNull()
    }
}