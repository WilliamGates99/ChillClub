package com.xeniac.chillclub.feature_settings.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.chillclub.MainCoroutineRule
import com.xeniac.chillclub.core.data.repositories.FakePreferencesRepositoryImpl
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
class GetNotificationPermissionCountUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakePreferencesRepository: FakePreferencesRepositoryImpl
    private lateinit var getNotificationPermissionCountUseCase: GetNotificationPermissionCountUseCase

    @Before
    fun setUp() {
        fakePreferencesRepository = FakePreferencesRepositoryImpl()
        getNotificationPermissionCountUseCase = GetNotificationPermissionCountUseCase(
            preferencesRepository = fakePreferencesRepository
        )
    }

    @Test
    fun getNotificationPermissionCount_returnsCurrentNotificationPermissionCountValue() = runTest {
        val currentNotificationPermissionCount = getNotificationPermissionCountUseCase().first()
        assertThat(currentNotificationPermissionCount).isEqualTo(fakePreferencesRepository.notificationPermissionCount)
    }
}