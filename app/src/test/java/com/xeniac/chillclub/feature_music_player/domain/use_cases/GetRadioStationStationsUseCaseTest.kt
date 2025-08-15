package com.xeniac.chillclub.feature_music_player.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.chillclub.MainCoroutineRule
import com.xeniac.chillclub.core.domain.models.Result
import com.xeniac.chillclub.feature_music_player.data.remote.repositories.FakeMusicPlayerRepositoryImpl
import com.xeniac.chillclub.feature_music_player.domain.errors.GetRadioStationsError
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
class GetRadioStationStationsUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeMusicPlayerRepository: FakeMusicPlayerRepositoryImpl
    private lateinit var getRadioStationsUseCase: GetRadioStationsUseCase

    @Before
    fun setUp() {
        fakeMusicPlayerRepository = FakeMusicPlayerRepositoryImpl()
        getRadioStationsUseCase = GetRadioStationsUseCase(
            musicPlayerRepository = fakeMusicPlayerRepository
        )
    }

    @Test
    fun getRadioStationsWithoutFetchFromRemoteWithCachedRadios_returnsSuccess() = runTest {
        fakeMusicPlayerRepository.addDummyRadioStations()

        val result = getRadioStationsUseCase(fetchFromRemote = false).first()
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun getRadioStationsWithoutFetchFromRemoteAndWithoutCachedRadiosWhenOffline_returnsError() =
        runTest {
            fakeMusicPlayerRepository.isNetworkAvailable(isAvailable = false)

            val result = getRadioStationsUseCase(fetchFromRemote = false).first()

            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).error).isInstanceOf(GetRadioStationsError.Network.Offline::class.java)
        }

    @Test
    fun getRadioStationsWithoutFetchFromRemoteAndWithoutCachedRadiosWhenOnline_returnsSuccess() =
        runTest {
            val result = getRadioStationsUseCase(fetchFromRemote = false).first()

            assertThat(result).isInstanceOf(Result.Success::class.java)
        }

    @Test
    fun getRadioStationsWithFetchFromRemoteWhenOffline_returnsError() = runTest {
        fakeMusicPlayerRepository.isNetworkAvailable(isAvailable = false)

        val result = getRadioStationsUseCase(fetchFromRemote = true).first()

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isInstanceOf(GetRadioStationsError.Network.Offline::class.java)
    }

    @Test
    fun getRadioStationsWithFetchFromRemoteWhenOnline_returnsSuccess() = runTest {
        val result = getRadioStationsUseCase(fetchFromRemote = true).first()

        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun getRadioStationsWithFetchFromRemoteWhenOnlineHttpStatusCodeOtherThanOk_returnsError() =
        runTest {
            fakeMusicPlayerRepository.setGetRadioStationsHttpStatusCode(HttpStatusCode.RequestTimeout)

            val result = getRadioStationsUseCase(fetchFromRemote = true).first()

            assertThat(result).isInstanceOf(Result.Error::class.java)
        }
}