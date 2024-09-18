package com.xeniac.chillclub.feature_music_player.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.chillclub.MainCoroutineRule
import com.xeniac.chillclub.feature_music_player.data.remote.repositories.FakeMusicPlayerRepositoryImpl
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
class ObserveMusicVolumeChangesUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeMusicPlayerRepositoryImpl: FakeMusicPlayerRepositoryImpl
    private lateinit var observeMusicVolumeChangesUseCase: ObserveMusicVolumeChangesUseCase

    @Before
    fun setUp() {
        fakeMusicPlayerRepositoryImpl = FakeMusicPlayerRepositoryImpl()
        observeMusicVolumeChangesUseCase = ObserveMusicVolumeChangesUseCase(
            musicPlayerRepository = fakeMusicPlayerRepositoryImpl
        )
    }

    @Test
    fun observeMusicVolumeChanges_returnsCurrentMusicVolume() = runTest {
        val currentMusicVolumePercentage = observeMusicVolumeChangesUseCase().first()
        assertThat(currentMusicVolumePercentage).isEqualTo(
            fakeMusicPlayerRepositoryImpl.musicVolumePercentage.first()
        )
    }
}