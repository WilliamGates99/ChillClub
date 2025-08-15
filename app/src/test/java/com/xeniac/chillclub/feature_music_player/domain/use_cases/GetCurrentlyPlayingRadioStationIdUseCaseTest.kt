package com.xeniac.chillclub.feature_music_player.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.chillclub.MainCoroutineRule
import com.xeniac.chillclub.core.data.repositories.FakeMusicPlayerDataStoreRepositoryImpl
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
class GetCurrentlyPlayingRadioStationIdUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeMusicPlayerDataStoreRepository: FakeMusicPlayerDataStoreRepositoryImpl
    private lateinit var getCurrentlyPlayingRadioStationIdUseCase: GetCurrentlyPlayingRadioStationIdUseCase

    @Before
    fun setUp() {
        fakeMusicPlayerDataStoreRepository = FakeMusicPlayerDataStoreRepositoryImpl()
        getCurrentlyPlayingRadioStationIdUseCase = GetCurrentlyPlayingRadioStationIdUseCase(
            musicPlayerDataStoreRepository = fakeMusicPlayerDataStoreRepository
        )
    }

    @Test
    fun getCurrentlyPlayingRadioStationId_returnsCurrentlyPlayingRadioStationId() = runTest {
        val radioStationId = getCurrentlyPlayingRadioStationIdUseCase().first()
        assertThat(radioStationId).isEqualTo(fakeMusicPlayerDataStoreRepository.currentlyPlayingRadioStationId.first())
    }
}