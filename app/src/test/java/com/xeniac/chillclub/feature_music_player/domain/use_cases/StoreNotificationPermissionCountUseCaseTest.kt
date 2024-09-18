package com.xeniac.chillclub.feature_music_player.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.chillclub.MainCoroutineRule
import com.xeniac.chillclub.core.data.repositories.FakeSettingsDataStoreRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class StoreNotificationPermissionCountUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeSettingsDataStoreRepository: FakeSettingsDataStoreRepositoryImpl
    private lateinit var storeNotificationPermissionCountUseCase: StoreNotificationPermissionCountUseCase
    private lateinit var getNotificationPermissionCountUseCase: GetNotificationPermissionCountUseCase

    @Before
    fun setUp() {
        fakeSettingsDataStoreRepository = FakeSettingsDataStoreRepositoryImpl()
        storeNotificationPermissionCountUseCase = StoreNotificationPermissionCountUseCase(
            settingsDataStoreRepository = fakeSettingsDataStoreRepository
        )
        getNotificationPermissionCountUseCase = GetNotificationPermissionCountUseCase(
            settingsDataStoreRepository = fakeSettingsDataStoreRepository
        )
    }

    @Test
    fun storeNotificationPermissionCount_returnsNewNotificationPermissionCount() = runTest {
        val testValue = 2
        storeNotificationPermissionCountUseCase(testValue)

        val notificationPermissionCount = getNotificationPermissionCountUseCase().first()
        assertThat(notificationPermissionCount).isEqualTo(testValue)
    }
}