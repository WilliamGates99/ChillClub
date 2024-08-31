package com.xeniac.chillclub.feature_music_player.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.chillclub.MainCoroutineRule
import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.feature_music_player.data.remote.repositories.FakeMusicPlayerRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class IncreaseMusicVolumeUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeMusicPlayerRepositoryImpl: FakeMusicPlayerRepositoryImpl
    private lateinit var increaseMusicVolumeUseCase: IncreaseMusicVolumeUseCase

    @Before
    fun setUp() {
        fakeMusicPlayerRepositoryImpl = FakeMusicPlayerRepositoryImpl()
        increaseMusicVolumeUseCase = IncreaseMusicVolumeUseCase(
            musicPlayerRepository = fakeMusicPlayerRepositoryImpl
        )
    }

    @Test
    fun increaseMusicVolume_returnsSuccess() = runTest {
        val result = increaseMusicVolumeUseCase()
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun increaseMusicVolume_increasesCurrentMusicVolumeByOne() = runTest {
        val musicVolumeBefore = fakeMusicPlayerRepositoryImpl.musicVolume.first()

        increaseMusicVolumeUseCase()

        val musicVolumeAfter = fakeMusicPlayerRepositoryImpl.musicVolume.first()
        assertThat(musicVolumeAfter).isEqualTo(musicVolumeBefore + 1)
    }
}