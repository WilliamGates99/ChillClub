package com.xeniac.chillclub.feature_settings.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.chillclub.MainCoroutineRule
import com.xeniac.chillclub.core.data.repositories.FakePreferencesRepositoryImpl
import com.xeniac.chillclub.core.domain.models.AppTheme
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
class SetCurrentAppThemeUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakePreferencesRepository: FakePreferencesRepositoryImpl
    private lateinit var setCurrentAppThemeUseCase: SetCurrentAppThemeUseCase
    private lateinit var getCurrentAppThemeUseCase: GetCurrentAppThemeUseCase

    @Before
    fun setUp() {
        fakePreferencesRepository = FakePreferencesRepositoryImpl()
        setCurrentAppThemeUseCase = SetCurrentAppThemeUseCase(
            preferencesRepository = fakePreferencesRepository
        )
        getCurrentAppThemeUseCase = GetCurrentAppThemeUseCase(
            preferencesRepository = fakePreferencesRepository
        )
    }

    @Test
    fun setCurrentAppTheme_returnsSuccess() = runTest {
        val testValue = AppTheme.Dark
        val result = setCurrentAppThemeUseCase(testValue)
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun setCurrentAppTheme_returnsNewAppTheme() = runTest {
        val testValue = AppTheme.Dark
        setCurrentAppThemeUseCase(testValue)

        val appTheme = getCurrentAppThemeUseCase().first()
        assertThat(appTheme).isEqualTo(testValue)
    }
}