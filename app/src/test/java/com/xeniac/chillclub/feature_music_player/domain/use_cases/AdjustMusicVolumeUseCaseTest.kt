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
class AdjustMusicVolumeUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeMusicPlayerRepositoryImpl: FakeMusicPlayerRepositoryImpl
    private lateinit var adjustMusicVolumeUseCase: AdjustMusicVolumeUseCase

    @Before
    fun setUp() {
        fakeMusicPlayerRepositoryImpl = FakeMusicPlayerRepositoryImpl()
        adjustMusicVolumeUseCase = AdjustMusicVolumeUseCase(
            musicPlayerRepository = fakeMusicPlayerRepositoryImpl
        )
    }

    @Test
    fun decreaseMusicVolume_returnsSuccess() = runTest {
        val currentMusicVolumePercentage = fakeMusicPlayerRepositoryImpl
            .musicVolumePercentage.first()
        val newMusicVolumePercentage = currentMusicVolumePercentage - 0.2f

        val result = adjustMusicVolumeUseCase(
            newPercentage = newMusicVolumePercentage,
            currentPercentage = currentMusicVolumePercentage
        ).first()
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun decreaseMusicVolume_decreasesCurrentMusicVolumeByOne() = runTest {
        val musicVolumeBefore = fakeMusicPlayerRepositoryImpl.currentMusicVolume
        val musicVolumePercentageBefore = fakeMusicPlayerRepositoryImpl
            .musicVolumePercentage.first()
        val newMusicVolumePercentage = musicVolumePercentageBefore - 0.2f

        val result = adjustMusicVolumeUseCase(
            newPercentage = newMusicVolumePercentage,
            currentPercentage = musicVolumePercentageBefore
        ).first()
        assertThat(result).isInstanceOf(Result.Success::class.java)

        val musicVolumeAfter = fakeMusicPlayerRepositoryImpl.currentMusicVolume
        val musicVolumePercentageAfter = fakeMusicPlayerRepositoryImpl.musicVolumePercentage.first()

        assertThat(musicVolumeAfter).isAtMost(musicVolumeBefore)
        assertThat(musicVolumePercentageAfter).isAtMost(musicVolumePercentageBefore)
    }

    @Test
    fun increaseMusicVolume_returnsSuccess() = runTest {
        val currentMusicVolumePercentage = fakeMusicPlayerRepositoryImpl
            .musicVolumePercentage.first()
        val newMusicVolumePercentage = currentMusicVolumePercentage + 0.2f

        val result = adjustMusicVolumeUseCase(
            newPercentage = newMusicVolumePercentage,
            currentPercentage = currentMusicVolumePercentage
        ).first()
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun increaseMusicVolume_increasesCurrentMusicVolumeByOne() = runTest {
        val musicVolumeBefore = fakeMusicPlayerRepositoryImpl.currentMusicVolume
        val musicVolumePercentageBefore = fakeMusicPlayerRepositoryImpl
            .musicVolumePercentage.first()
        val newMusicVolumePercentage = musicVolumePercentageBefore + 0.2f

        val result = adjustMusicVolumeUseCase(
            newPercentage = newMusicVolumePercentage,
            currentPercentage = musicVolumePercentageBefore
        ).first()
        assertThat(result).isInstanceOf(Result.Success::class.java)

        val musicVolumeAfter = fakeMusicPlayerRepositoryImpl.currentMusicVolume
        val musicVolumePercentageAfter = fakeMusicPlayerRepositoryImpl.musicVolumePercentage.first()

        assertThat(musicVolumeAfter).isAtLeast(musicVolumeBefore)
        assertThat(musicVolumePercentageAfter).isAtLeast(musicVolumePercentageBefore)
    }
}