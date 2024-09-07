package com.xeniac.chillclub.feature_music_player.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.chillclub.MainCoroutineRule
import com.xeniac.chillclub.core.domain.utils.Result
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
class DecreaseMusicVolumeUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeMusicPlayerRepositoryImpl: FakeMusicPlayerRepositoryImpl
    private lateinit var decreaseMusicVolumeUseCase: DecreaseMusicVolumeUseCase

    @Before
    fun setUp() {
        fakeMusicPlayerRepositoryImpl = FakeMusicPlayerRepositoryImpl()
        decreaseMusicVolumeUseCase = DecreaseMusicVolumeUseCase(
            musicPlayerRepository = fakeMusicPlayerRepositoryImpl
        )
    }

    @Test
    fun decreaseMusicVolume_returnsSuccess() = runTest {
        val result = decreaseMusicVolumeUseCase().first()
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun decreaseMusicVolume_decreasesCurrentMusicVolumeByOne() = runTest {
        val musicVolumeBefore = fakeMusicPlayerRepositoryImpl.currentMusicVolume
        val musicVolumePercentageBefore =
            fakeMusicPlayerRepositoryImpl.musicVolumePercentage.first()

        val result = decreaseMusicVolumeUseCase().first()
        assertThat(result).isInstanceOf(Result.Success::class.java)

        val musicVolumeAfter = fakeMusicPlayerRepositoryImpl.currentMusicVolume
        val musicVolumePercentageAfter = fakeMusicPlayerRepositoryImpl.musicVolumePercentage.first()

        assertThat(musicVolumeAfter).isEqualTo(musicVolumeBefore - 1)
        assertThat(musicVolumePercentageAfter).isAtMost(musicVolumePercentageBefore)
    }
}