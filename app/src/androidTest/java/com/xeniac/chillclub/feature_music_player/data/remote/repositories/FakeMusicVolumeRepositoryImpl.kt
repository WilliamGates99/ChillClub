package com.xeniac.chillclub.feature_music_player.data.remote.repositories

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xeniac.chillclub.core.domain.models.Result
import com.xeniac.chillclub.core.domain.utils.scaleToUnitInterval
import com.xeniac.chillclub.feature_music_player.domain.errors.AdjustVolumeError
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumePercentage
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.math.roundToInt

class FakeMusicVolumeRepositoryImpl @Inject constructor() : MusicVolumeRepository {

    private var minVolume = 0
    private var maxVolume = 15

    var currentMusicVolume = 7
    var musicVolumePercentage = SnapshotStateList<MusicVolumePercentage>().apply {
        add(
            currentMusicVolume.scaleToUnitInterval(
                minValue = minVolume,
                maxValue = maxVolume
            )
        )
    }

    override fun observeMusicVolumeChanges(): Flow<MusicVolumePercentage> = snapshotFlow {
        musicVolumePercentage.first()
    }

    override suspend fun adjustMusicVolume(
        newPercentage: MusicVolumePercentage,
        currentPercentage: MusicVolumePercentage
    ): Result<Unit, AdjustVolumeError> {
        return try {
            // Calculate the percentage difference
            val deltaPercentage = newPercentage - currentPercentage

            // Calculate how much to adjust the music volume
            val volumeRange = maxVolume - minVolume
            val deltaVolume = (deltaPercentage * volumeRange).roundToInt()

            musicVolumePercentage.apply {
                currentMusicVolume += deltaVolume
                val newMusicVolumePercentage = (currentMusicVolume).scaleToUnitInterval(
                    minValue = minVolume,
                    maxValue = maxVolume
                )

                clear()
                add(newMusicVolumePercentage)
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(AdjustVolumeError.SomethingWentWrong)
        }
    }
}