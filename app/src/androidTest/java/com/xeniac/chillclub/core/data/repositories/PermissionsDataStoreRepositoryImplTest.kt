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
import com.xeniac.chillclub.core.domain.models.PermissionsPreferences
import com.xeniac.chillclub.core.domain.models.PermissionsPreferencesSerializer
import com.xeniac.chillclub.core.domain.repositories.PermissionsDataStoreRepository
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
class PermissionsDataStoreRepositoryImplTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val testScope: TestScope = TestScope(context = testDispatcher)

    private val testDataStore: DataStore<PermissionsPreferences> = DataStoreFactory.create(
        serializer = PermissionsPreferencesSerializer,
        corruptionHandler = ReplaceFileCorruptionHandler { PermissionsPreferences() },
        scope = testScope.backgroundScope,
        produceFile = { context.preferencesDataStoreFile(name = "Permissions-Test.pb") }
    )

    private val testRepository: PermissionsDataStoreRepository = PermissionsDataStoreRepositoryImpl(
        dataStore = testDataStore
    )

    @Before
    fun setUp() {
        testScope.launch {
            testDataStore.updateData { PermissionsPreferences() }
        }
    }

    @After
    fun tearDown() {
        testScope.cancel()
    }

    /*
    Fetch Initial Preferences Test Cases:
    getNotificationPermissionCount -> 0
     */
    @Test
    fun fetchInitialPreferences() = testScope.runTest {
        val initialNotificationPermissionCount = testRepository
            .getNotificationPermissionCount().first()

        assertThat(initialNotificationPermissionCount).isEqualTo(0)
    }

    @Test
    fun writeNotificationPermissionCount() = testScope.runTest {
        val testValue = 2
        testRepository.storeNotificationPermissionCount(count = testValue)

        val notificationPermissionCount = testRepository.getNotificationPermissionCount().first()
        assertThat(notificationPermissionCount).isEqualTo(testValue)
    }
}