package com.xeniac.chillclub.feature_music_player.data.repositories

import androidx.room.withTransaction
import com.xeniac.chillclub.core.data.local.ChillClubDatabase
import com.xeniac.chillclub.core.data.local.RadioStationsDao
import com.xeniac.chillclub.core.data.local.entities.RadioStationsVersionEntity
import com.xeniac.chillclub.core.data.mappers.toRadioStation
import com.xeniac.chillclub.core.data.mappers.toRadioStationEntity
import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.core.domain.models.Result
import com.xeniac.chillclub.core.domain.repositories.MusicPlayerDataStoreRepository
import com.xeniac.chillclub.feature_music_player.data.remote.dto.GetRadioStationsResponseDto
import com.xeniac.chillclub.feature_music_player.domain.errors.GetRadioStationsError
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import dagger.Lazy
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import timber.log.Timber
import java.net.UnknownHostException
import java.security.cert.CertPathValidatorException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException
import kotlin.coroutines.coroutineContext

class MusicPlayerRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val database: Lazy<ChillClubDatabase>,
    private val dao: Lazy<RadioStationsDao>,
    private val musicPlayerDataStoreRepository: Lazy<MusicPlayerDataStoreRepository>
) : MusicPlayerRepository {

    override fun getRadioStations(
        fetchFromRemote: Boolean
    ): Flow<Result<List<RadioStation>, GetRadioStationsError>> = flow {
        return@flow try {
            val localRadioStationEntities = dao.get().getRadioStations()
            emit(Result.Success(localRadioStationEntities.map { it.toRadioStation() }))

            val shouldJustLoadFromCache = localRadioStationEntities.isNotEmpty() && !fetchFromRemote
            if (shouldJustLoadFromCache) {
                return@flow
            }

            val httpResponse = httpClient.get(
                urlString = MusicPlayerRepository.EndPoints.GetRadioStations.url
            )

            Timber.i("Get radio stations response call = ${httpResponse.request.call}")

            when (httpResponse.status) {
                HttpStatusCode.OK -> {
                    val responseDto = httpResponse.body<GetRadioStationsResponseDto>()

                    val radioStationsVersions = dao.get().getRadioStationsVersions()
                    val isFirstTimeFetchingRadioStations = radioStationsVersions.isEmpty()
                    val shouldReplaceAllRadioStations = when {
                        isFirstTimeFetchingRadioStations -> true
                        else -> {
                            val currentVersion = radioStationsVersions.first().version
                            val newVersion = responseDto.version
                            val isLocalRadioStationsOutdated = currentVersion < newVersion
                            isLocalRadioStationsOutdated
                        }
                    }

                    if (!shouldReplaceAllRadioStations) {
                        emit(Result.Success(localRadioStationEntities.map { it.toRadioStation() }))
                        return@flow
                    }

                    database.get().withTransaction {
                        musicPlayerDataStoreRepository.get().removeCurrentlyPlayingRadioStationId()

                        dao.get().replaceAllRadioStations(
                            radioStationsVersionEntity = RadioStationsVersionEntity(
                                version = responseDto.version
                            ),
                            radioStationEntities = responseDto.radioStationDtos.map {
                                it.toRadioStationEntity()
                            }
                        )
                    }

                    val radioStations = dao.get().getRadioStations().map { it.toRadioStation() }
                    emit(Result.Success(radioStations))
                }
                else -> emit(Result.Error(GetRadioStationsError.Network.SomethingWentWrong))
            }
        } catch (e: UnresolvedAddressException) { // When device is offline
            Timber.e("Get radio stations UnresolvedAddressException:")
            e.printStackTrace()
            emit(Result.Error(GetRadioStationsError.Network.Offline))
        } catch (e: UnknownHostException) { // When device is offline
            Timber.e("Get radio stations UnknownHostException:")
            e.printStackTrace()
            emit(Result.Error(GetRadioStationsError.Network.Offline))
        } catch (e: ConnectTimeoutException) {
            Timber.e("Get radio stations ConnectTimeoutException:")
            e.printStackTrace()
            emit(Result.Error(GetRadioStationsError.Network.ConnectTimeoutException))
        } catch (e: HttpRequestTimeoutException) {
            Timber.e("Get radio stations HttpRequestTimeoutException:")
            e.printStackTrace()
            emit(Result.Error(GetRadioStationsError.Network.HttpRequestTimeoutException))
        } catch (e: SocketTimeoutException) {
            Timber.e("Get radio stations SocketTimeoutException:")
            e.printStackTrace()
            emit(Result.Error(GetRadioStationsError.Network.SocketTimeoutException))
        } catch (e: RedirectResponseException) { // 3xx responses
            Timber.e("Get radio stations RedirectResponseException:")
            e.printStackTrace()
            emit(Result.Error(GetRadioStationsError.Network.RedirectResponseException))
        } catch (e: ClientRequestException) { // 4xx responses
            Timber.e("Get radio stations ClientRequestException:")
            e.printStackTrace()
            when (e.response.status) {
                HttpStatusCode.TooManyRequests -> emit(Result.Error(GetRadioStationsError.Network.TooManyRequests))
                else -> emit(Result.Error(GetRadioStationsError.Network.ClientRequestException))
            }
        } catch (e: ServerResponseException) { // 5xx responses
            Timber.e("Get radio stations ServerResponseException:")
            e.printStackTrace()
            emit(Result.Error(GetRadioStationsError.Network.ServerResponseException))
        } catch (e: SerializationException) {
            Timber.e("Get radio stations SerializationException:")
            e.printStackTrace()
            emit(Result.Error(GetRadioStationsError.Network.SerializationException))
        } catch (e: JsonConvertException) {
            Timber.e("Get radio stations JsonConvertException:")
            e.printStackTrace()
            emit(Result.Error(GetRadioStationsError.Network.JsonConvertException))
        } catch (e: SSLHandshakeException) {
            Timber.e("Get radio stations SSLHandshakeException:")
            e.printStackTrace()
            emit(Result.Error(GetRadioStationsError.Network.SSLHandshakeException))
        } catch (e: CertPathValidatorException) {
            Timber.e("Get radio stations CertPathValidatorException:")
            e.printStackTrace()
            emit(Result.Error(GetRadioStationsError.Network.SSLHandshakeException))
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            Timber.e("Get radio stations Exception:")
            e.printStackTrace()

            val isOfflineError = e.message?.contains(
                other = "unable to resolve host",
                ignoreCase = true
            ) == true

            when {
                isOfflineError -> emit(Result.Error(GetRadioStationsError.Network.Offline))
                else -> emit(Result.Error(GetRadioStationsError.Network.SomethingWentWrong))
            }
        }
    }

    override fun getCurrentlyPlayingRadioStation(
        radioStationId: Long
    ): Flow<RadioStation?> = dao.get().observeRadioStation(id = radioStationId).map {
        it?.toRadioStation()
    }
}