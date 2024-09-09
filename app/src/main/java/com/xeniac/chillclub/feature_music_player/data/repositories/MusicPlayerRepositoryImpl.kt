package com.xeniac.chillclub.feature_music_player.data.repositories

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Build
import androidx.room.withTransaction
import com.xeniac.chillclub.core.data.local.ChillClubDatabase
import com.xeniac.chillclub.core.data.local.RadioStationsDao
import com.xeniac.chillclub.core.data.utils.scaleToUnitInterval
import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.feature_music_player.data.remote.dto.GetRadioStationsResponseDto
import com.xeniac.chillclub.feature_music_player.di.MUSIC_STREAM_TYPE
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumePercentage
import com.xeniac.chillclub.feature_music_player.domain.utils.AdjustVolumeError
import com.xeniac.chillclub.feature_music_player.domain.utils.GetRadioStationsError
import dagger.Lazy
import dagger.hilt.android.qualifiers.ApplicationContext
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
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import kotlin.math.roundToInt

class MusicPlayerRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val httpClient: HttpClient,
    private val db: Lazy<ChillClubDatabase>,
    private val dao: Lazy<RadioStationsDao>,
    private val audioManager: AudioManager,
    private val streamType: MUSIC_STREAM_TYPE
) : MusicPlayerRepository {

    override fun observeMusicVolumeChanges(): Flow<MusicVolumePercentage> = callbackFlow {
        val maxVolume = audioManager.getStreamMaxVolume(streamType)
        val minVolume = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            audioManager.getStreamMinVolume(streamType)
        } else 0
        val currentVolumePercentage = audioManager.getStreamVolume(streamType).scaleToUnitInterval(
            minValue = minVolume,
            maxValue = maxVolume
        )

        trySend(currentVolumePercentage)

        val volumeChangedActionReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val volumeStreamType = intent?.getIntExtra(
                    /* name = */ "android.media.EXTRA_VOLUME_STREAM_TYPE",
                    /* defaultValue = */ 0
                )

                when (volumeStreamType) {
                    streamType -> {
                        val newVolumePercentage = audioManager
                            .getStreamVolume(streamType)
                            .scaleToUnitInterval(
                                minValue = minVolume,
                                maxValue = maxVolume
                            )
                        trySend(newVolumePercentage)
                    }
                }
            }
        }

        context.registerReceiver(
            /* receiver = */ volumeChangedActionReceiver,
            /* filter = */ IntentFilter(/* action = */ "android.media.VOLUME_CHANGED_ACTION")
        )

        awaitClose {
            context.unregisterReceiver(volumeChangedActionReceiver)
        }
    }

    override fun adjustMusicVolume(
        newPercentage: MusicVolumePercentage,
        currentPercentage: MusicVolumePercentage
    ): Flow<Result<Unit, AdjustVolumeError>> = flow {
        try {
            val currentVolume = audioManager.getStreamVolume(streamType)
            val maxVolume = audioManager.getStreamMaxVolume(streamType)
            val minVolume = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                audioManager.getStreamMinVolume(streamType)
            } else 0

            // Calculate the percentage difference
            val deltaPercentage = newPercentage - currentPercentage

            // Calculate how much to adjust the music volume
            val volumeRange = maxVolume - minVolume
            val deltaVolume = (deltaPercentage * volumeRange).roundToInt()

            audioManager.setStreamVolume(
                /* streamType = */ streamType,
                /* index = */ currentVolume.plus(deltaVolume).coerceIn(
                    minimumValue = minVolume,
                    maximumValue = maxVolume
                ),
                /* flags = */ 0 // Do not show the volume slider
            )

            emit(Result.Success(Unit))
        } catch (e: Exception) {
            coroutineContext.ensureActive()

            Timber.i("Adjust music volume failed:")
            e.printStackTrace()
            emit(Result.Error(AdjustVolumeError.SomethingWentWrong))
        }
    }

    override fun getRadioStations(
        fetchFromRemote: Boolean
    ): Flow<Result<List<RadioStation>, GetRadioStationsError>> = flow {
        try {
            val localRadioStationEntities = dao.get().getRadioStations()

            val shouldJustLoadFromCache = localRadioStationEntities.isNotEmpty() && !fetchFromRemote
            if (shouldJustLoadFromCache) {
                emit(Result.Success(localRadioStationEntities.map { it.toRadioStation() }))
                return@flow
            }

            val response = httpClient.get(
                urlString = MusicPlayerRepository.EndPoints.GetRadioStations.url
            )

            Timber.i("Get radio stations response call = ${response.request.call}")

            when (response.status) {
                HttpStatusCode.OK -> {
                    val remoteRadioStationDtos = response
                        .body<GetRadioStationsResponseDto>()
                        .radioStationDtos

                    db.get().withTransaction {
                        dao.get().clearRadioStations()
                        dao.get().insertRadioStations(
                            radioStationEntities = remoteRadioStationDtos.map { it.toRadioStationEntity() }
                        )
                    }

                    val radioStations = dao.get().getRadioStations().map { it.toRadioStation() }
                    emit(Result.Success(radioStations))
                }
                else -> emit(Result.Error(GetRadioStationsError.Network.SomethingWentWrong))
            }
        } catch (e: UnresolvedAddressException) { // When device is offline
            Timber.e("Get radio stations UnresolvedAddressException:}")
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
            emit(Result.Error(GetRadioStationsError.Network.ClientRequestException))
        } catch (e: ServerResponseException) { // 5xx responses
            Timber.e("Get radio stations ServerResponseException:")
            e.printStackTrace()
            emit(Result.Error(GetRadioStationsError.Network.ServerResponseException))
        } catch (e: SerializationException) {
            Timber.e("Get radio stations SerializationException:")
            e.printStackTrace()
            emit(Result.Error(GetRadioStationsError.Network.SerializationException))
        } catch (e: Exception) {
            coroutineContext.ensureActive()

            Timber.e("Get radio stations Exception:")
            e.printStackTrace()
            if (e.message?.lowercase(Locale.US)?.contains("unable to resolve host") == true) {
                emit(Result.Error(GetRadioStationsError.Network.Offline))
            } else emit(Result.Error(GetRadioStationsError.Network.SomethingWentWrong))
        }
    }
}