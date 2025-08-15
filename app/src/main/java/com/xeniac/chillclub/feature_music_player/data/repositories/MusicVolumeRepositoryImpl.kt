package com.xeniac.chillclub.feature_music_player.data.repositories

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Build
import com.xeniac.chillclub.core.domain.models.Result
import com.xeniac.chillclub.core.domain.utils.scaleToUnitInterval
import com.xeniac.chillclub.feature_music_player.domain.errors.AdjustVolumeError
import com.xeniac.chillclub.feature_music_player.di.MUSIC_STREAM_TYPE
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumePercentage
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumeRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import kotlin.math.roundToInt

class MusicVolumeRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val audioManager: AudioManager,
    private val musicStreamType: MUSIC_STREAM_TYPE
) : MusicVolumeRepository {

    override fun observeMusicVolumeChanges(): Flow<MusicVolumePercentage> = callbackFlow {
        val maxVolume = audioManager.getStreamMaxVolume(musicStreamType)
        val minVolume = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            audioManager.getStreamMinVolume(musicStreamType)
        } else 0
        val currentVolumePercentage = audioManager
            .getStreamVolume(musicStreamType)
            .scaleToUnitInterval(
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
                    musicStreamType -> {
                        val newVolumePercentage = audioManager
                            .getStreamVolume(musicStreamType)
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

    override suspend fun adjustMusicVolume(
        newPercentage: MusicVolumePercentage,
        currentPercentage: MusicVolumePercentage
    ): Result<Unit, AdjustVolumeError> {
        return try {
            val currentVolume = audioManager.getStreamVolume(musicStreamType)
            val maxVolume = audioManager.getStreamMaxVolume(musicStreamType)
            val minVolume = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                audioManager.getStreamMinVolume(musicStreamType)
            } else 0

            // Calculate the percentage difference
            val percentageChange = newPercentage - currentPercentage

            // Calculate how much to adjust the music volume
            val volumeRange = maxVolume - minVolume
            val volumeChange = (percentageChange * volumeRange).roundToInt()
            val newVolumeIndex = currentVolume.plus(volumeChange).coerceIn(
                minimumValue = minVolume,
                maximumValue = maxVolume
            )

            audioManager.setStreamVolume(
                /* streamType = */ musicStreamType,
                /* index = */ newVolumeIndex,
                /* flags = */ 0 // Do not show the volume slider
            )

            Result.Success(Unit)
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            Timber.e("Adjust music volume failed:")
            e.printStackTrace()
            Result.Error(AdjustVolumeError.SomethingWentWrong)
        }
    }
}