package com.xeniac.chillclub.feature_music_player.data.remote.repositories

import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.feature_music_player.data.remote.dto.GetRadiosResponseDto
import com.xeniac.chillclub.feature_music_player.domain.models.Radio
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import com.xeniac.chillclub.feature_music_player.domain.utils.GetRadiosError
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
import kotlinx.serialization.SerializationException
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class MusicPlayerRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient
) : MusicPlayerRepository {

    override suspend fun getRadios(): Result<List<Radio>, GetRadiosError> = try {
        val response = httpClient.get(urlString = MusicPlayerRepository.EndPoints.GetRadios.url)

        Timber.i("Get radios response call = ${response.request.call}")

        when (response.status) {
            HttpStatusCode.OK -> {
                val getRadiosResponseDto = response.body<GetRadiosResponseDto>()
                val radios = getRadiosResponseDto.toList().map { it.toRadio() }
                Result.Success(radios)
            }
            else -> Result.Error(GetRadiosError.Network.SomethingWentWrong)
        }
    } catch (e: CancellationException) {
        Timber.e("Get radios CancellationException:")
        e.printStackTrace()
        Result.Error(GetRadiosError.CancellationException)
    } catch (e: UnresolvedAddressException) { // When device is offline
        Timber.e("Get radios UnresolvedAddressException:}")
        e.printStackTrace()
        Result.Error(GetRadiosError.Network.Offline)
    } catch (e: ConnectTimeoutException) {
        Timber.e("Get radios ConnectTimeoutException:")
        e.printStackTrace()
        Result.Error(GetRadiosError.Network.ConnectTimeoutException)
    } catch (e: HttpRequestTimeoutException) {
        Timber.e("Get radios HttpRequestTimeoutException:")
        e.printStackTrace()
        Result.Error(GetRadiosError.Network.HttpRequestTimeoutException)
    } catch (e: SocketTimeoutException) {
        Timber.e("Get radios SocketTimeoutException:")
        e.printStackTrace()
        Result.Error(GetRadiosError.Network.SocketTimeoutException)
    } catch (e: RedirectResponseException) { // 3xx responses
        Timber.e("Get radios RedirectResponseException:")
        e.printStackTrace()
        Result.Error(GetRadiosError.Network.RedirectResponseException)
    } catch (e: ClientRequestException) { // 4xx responses
        Timber.e("Get radios ClientRequestException:")
        e.printStackTrace()
        Result.Error(GetRadiosError.Network.ClientRequestException)
    } catch (e: ServerResponseException) { // 5xx responses
        Timber.e("Get radios ServerResponseException:")
        e.printStackTrace()
        Result.Error(GetRadiosError.Network.ServerResponseException)
    } catch (e: SerializationException) {
        Timber.e("Get radios SerializationException:")
        e.printStackTrace()
        Result.Error(GetRadiosError.Network.SerializationException)
    } catch (e: Exception) {
        Timber.e("Get radios Exception:")
        e.printStackTrace()
        if (e.message?.lowercase(Locale.US)?.contains("unable to resolve host") == true) {
            Result.Error(GetRadiosError.Network.Offline)
        } else Result.Error(GetRadiosError.Network.SomethingWentWrong)
    }
}