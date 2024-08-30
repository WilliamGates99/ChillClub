package com.xeniac.chillclub.feature_music_player.data.repositories

import androidx.room.withTransaction
import com.xeniac.chillclub.core.data.local.ChillClubDao
import com.xeniac.chillclub.core.data.local.ChillClubDatabase
import com.xeniac.chillclub.core.domain.models.Radio
import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.feature_music_player.data.remote.dto.GetRadiosResponseDto
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import com.xeniac.chillclub.feature_music_player.domain.utils.GetRadiosError
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
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class MusicPlayerRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val db: Lazy<ChillClubDatabase>,
    private val dao: Lazy<ChillClubDao>
) : MusicPlayerRepository {

    override suspend fun getRadios(
        fetchFromRemote: Boolean
    ): Flow<Result<List<Radio>, GetRadiosError>> = flow {
        val localRadioEntities = dao.get().getRadios()

        val shouldJustLoadFromCache = localRadioEntities.isNotEmpty() && !fetchFromRemote
        if (shouldJustLoadFromCache) {
            emit(Result.Success(localRadioEntities.map { it.toRadio() }))
            return@flow
        }

        try {
            val response = httpClient.get(urlString = MusicPlayerRepository.EndPoints.GetRadios.url)

            Timber.i("Get radios response call = ${response.request.call}")

            when (response.status) {
                HttpStatusCode.OK -> {
                    val remoteRadioDtos = response.body<GetRadiosResponseDto>().radioDtos

                    db.get().withTransaction {
                        dao.get().clearRadios()
                        dao.get().insertRadios(
                            radioEntities = remoteRadioDtos.map { it.toRadioEntity() }
                        )
                    }

                    val radios = dao.get().getRadios().map { it.toRadio() }
                    emit(Result.Success(radios))
                }
                else -> emit(Result.Error(GetRadiosError.Network.SomethingWentWrong))
            }
        } catch (e: UnresolvedAddressException) { // When device is offline
            Timber.e("Get radios UnresolvedAddressException:}")
            e.printStackTrace()
            emit(Result.Error(GetRadiosError.Network.Offline))
        } catch (e: ConnectTimeoutException) {
            Timber.e("Get radios ConnectTimeoutException:")
            e.printStackTrace()
            emit(Result.Error(GetRadiosError.Network.ConnectTimeoutException))
        } catch (e: HttpRequestTimeoutException) {
            Timber.e("Get radios HttpRequestTimeoutException:")
            e.printStackTrace()
            emit(Result.Error(GetRadiosError.Network.HttpRequestTimeoutException))
        } catch (e: SocketTimeoutException) {
            Timber.e("Get radios SocketTimeoutException:")
            e.printStackTrace()
            emit(Result.Error(GetRadiosError.Network.SocketTimeoutException))
        } catch (e: RedirectResponseException) { // 3xx responses
            Timber.e("Get radios RedirectResponseException:")
            e.printStackTrace()
            emit(Result.Error(GetRadiosError.Network.RedirectResponseException))
        } catch (e: ClientRequestException) { // 4xx responses
            Timber.e("Get radios ClientRequestException:")
            e.printStackTrace()
            emit(Result.Error(GetRadiosError.Network.ClientRequestException))
        } catch (e: ServerResponseException) { // 5xx responses
            Timber.e("Get radios ServerResponseException:")
            e.printStackTrace()
            emit(Result.Error(GetRadiosError.Network.ServerResponseException))
        } catch (e: SerializationException) {
            Timber.e("Get radios SerializationException:")
            e.printStackTrace()
            emit(Result.Error(GetRadiosError.Network.SerializationException))
        } catch (e: Exception) {
            coroutineContext.ensureActive()

            Timber.e("Get radios Exception:")
            e.printStackTrace()
            if (e.message?.lowercase(Locale.US)?.contains("unable to resolve host") == true) {
                emit(Result.Error(GetRadiosError.Network.Offline))
            } else emit(Result.Error(GetRadiosError.Network.SomethingWentWrong))
        }
    }
}