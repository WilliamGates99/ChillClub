package com.xeniac.chillclub.core.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.chillclub.MainCoroutineRule
import com.xeniac.chillclub.core.data.repositories.FakePreferencesRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class GetCurrentAppLocaleUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakePreferencesRepository: FakePreferencesRepositoryImpl
    private lateinit var getCurrentAppLocaleUseCase: GetCurrentAppLocaleUseCase

    @Before
    fun setUp() {
        fakePreferencesRepository = FakePreferencesRepositoryImpl()
        getCurrentAppLocaleUseCase = GetCurrentAppLocaleUseCase(
            preferencesRepository = fakePreferencesRepository
        )
    }

    @Test
    fun getCurrentAppLocale_returnsCurrentAppLocaleValue() = runTest {
        val currentAppLocale = getCurrentAppLocaleUseCase()
        assertThat(currentAppLocale).isEqualTo(fakePreferencesRepository.appLocale)
    }
}