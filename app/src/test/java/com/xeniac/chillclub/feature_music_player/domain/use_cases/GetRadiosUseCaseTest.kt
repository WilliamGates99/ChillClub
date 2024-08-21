package com.xeniac.chillclub.feature_music_player.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.chillclub.MainCoroutineRule
import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.feature_music_player.data.remote.repositories.FakeMusicPlayerRepositoryImpl
import com.xeniac.chillclub.feature_music_player.domain.utils.GetRadiosError
import io.ktor.http.HttpStatusCode
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
class GetRadiosUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeMusicPlayerRepository: FakeMusicPlayerRepositoryImpl
    private lateinit var getRadiosUseCase: GetRadiosUseCase

    @Before
    fun setUp() {
        fakeMusicPlayerRepository = FakeMusicPlayerRepositoryImpl()
        getRadiosUseCase = GetRadiosUseCase(
            musicPlayerRepository = fakeMusicPlayerRepository
        )
    }

    @Test
    fun getRadiosWithoutFetchFromRemoteWithCachedRadios_returnsSuccess() = runTest {
        fakeMusicPlayerRepository.addDummyRadios()

        val result = getRadiosUseCase(fetchFromRemote = false).first()
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun getRadiosWithoutFetchFromRemoteAndWithoutCachedRadiosWhenOffline_returnsError() = runTest {
        fakeMusicPlayerRepository.isNetworkAvailable(isAvailable = false)

        val result = getRadiosUseCase(fetchFromRemote = false).first()

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isInstanceOf(GetRadiosError.Network.Offline::class.java)
    }

    @Test
    fun getRadiosWithoutFetchFromRemoteAndWithoutCachedRadiosWhenOnline_returnsSuccess() = runTest {
        val result = getRadiosUseCase(fetchFromRemote = false).first()

        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun getRadiosWithFetchFromRemoteWhenOffline_returnsError() = runTest {
        fakeMusicPlayerRepository.isNetworkAvailable(isAvailable = false)

        val result = getRadiosUseCase(fetchFromRemote = true).first()

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isInstanceOf(GetRadiosError.Network.Offline::class.java)
    }

    @Test
    fun getRadiosWithFetchFromRemoteWhenOnline_returnsSuccess() = runTest {
        val result = getRadiosUseCase(fetchFromRemote = true).first()

        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun getRadiosWithFetchFromRemoteWhenOnlineHttpStatusCodeOtherThanOk_returnsError() = runTest {
        fakeMusicPlayerRepository.setGetRadiosHttpStatusCode(HttpStatusCode.RequestTimeout)

        val result = getRadiosUseCase(fetchFromRemote = true).first()

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }
}