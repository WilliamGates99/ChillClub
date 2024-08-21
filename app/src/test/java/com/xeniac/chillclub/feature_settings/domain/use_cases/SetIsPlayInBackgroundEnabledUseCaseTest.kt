package com.xeniac.chillclub.feature_settings.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.chillclub.MainCoroutineRule
import com.xeniac.chillclub.core.data.local.repositories.FakePreferencesRepositoryImpl
import com.xeniac.chillclub.core.domain.utils.Result
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
class SetIsPlayInBackgroundEnabledUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakePreferencesRepository: FakePreferencesRepositoryImpl
    private lateinit var setIsPlayInBackgroundEnabledUseCase: SetIsPlayInBackgroundEnabledUseCase
    private lateinit var getIsPlayInBackgroundEnabledUseCase: GetIsPlayInBackgroundEnabledUseCase

    @Before
    fun setUp() {
        fakePreferencesRepository = FakePreferencesRepositoryImpl()
        setIsPlayInBackgroundEnabledUseCase = SetIsPlayInBackgroundEnabledUseCase(
            preferencesRepository = fakePreferencesRepository
        )
        getIsPlayInBackgroundEnabledUseCase = GetIsPlayInBackgroundEnabledUseCase(
            preferencesRepository = fakePreferencesRepository
        )
    }

    @Test
    fun setsPlayInBackgroundEnabled_returnsSuccess() = runTest {
        val testValue = false
        val result = setIsPlayInBackgroundEnabledUseCase(testValue)
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun setsPlayInBackgroundEnabled_returnsNewIsPlayInBackgroundEnabled() = runTest {
        val testValue = false
        setIsPlayInBackgroundEnabledUseCase(testValue)

        val isPlayInBackgroundEnabled = getIsPlayInBackgroundEnabledUseCase().first()
        assertThat(isPlayInBackgroundEnabled).isEqualTo(testValue)
    }
}