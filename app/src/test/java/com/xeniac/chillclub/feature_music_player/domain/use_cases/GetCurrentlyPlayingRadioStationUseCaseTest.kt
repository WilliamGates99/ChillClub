package com.xeniac.chillclub.feature_music_player.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.chillclub.MainCoroutineRule
import com.xeniac.chillclub.feature_music_player.data.remote.repositories.FakeMusicPlayerRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class GetCurrentlyPlayingRadioStationUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeMusicPlayerRepository: FakeMusicPlayerRepositoryImpl
    private lateinit var getCurrentlyPlayingRadioStationUseCase: GetCurrentlyPlayingRadioStationUseCase

    @Before
    fun setUp() {
        fakeMusicPlayerRepository = FakeMusicPlayerRepositoryImpl()
        getCurrentlyPlayingRadioStationUseCase = GetCurrentlyPlayingRadioStationUseCase(
            musicPlayerRepository = fakeMusicPlayerRepository
        )
    }

    @Test
    fun getCurrentlyPlayingRadioStationWithoutCachedRadios_returnsNull() = runTest {
        val radioStationId = 5L

        val radioStation = getCurrentlyPlayingRadioStationUseCase(radioStationId).firstOrNull()
        assertThat(radioStation).isNull()
    }

    @Test
    fun getCurrentlyPlayingRadioStationWithCachedRadios_returnsCurrentlyPlayingRadioStation() =
        runTest {
            fakeMusicPlayerRepository.addDummyRadioStations()

            val radioStationId = 5L

            val radioStation = getCurrentlyPlayingRadioStationUseCase(radioStationId).firstOrNull()
            assertThat(radioStation).isNotNull()
            assertThat(radioStation?.id).isEqualTo(radioStationId)
        }

    @Test
    fun getCurrentlyPlayingRadioStationWithCachedRadiosAndInvalidId_returnsNull() = runTest {
        fakeMusicPlayerRepository.addDummyRadioStations()

        val radioStationId = 20L

        val radioStation = getCurrentlyPlayingRadioStationUseCase(radioStationId).firstOrNull()
        assertThat(radioStation).isNull()
    }
}