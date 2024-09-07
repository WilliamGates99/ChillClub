package com.xeniac.chillclub.feature_music_player.presensation.states

import android.os.Parcelable
import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumePercentage
import kotlinx.parcelize.Parcelize

@Parcelize
data class MusicPlayerState(
    val isRadioStationsLoading: Boolean = false,
    val isMusicBuffering: Boolean = false,
    val isMusicPlaying: Boolean = false,
    val musicVolumePercentage: MusicVolumePercentage? = null,
    val currentRadioStations: RadioStation? = null,
    val radioStations: List<RadioStation> = emptyList(),
    val isPermissionDialogVisible: Boolean = false,
    val notificationPermissionCount: Int = 0,
    val permissionDialogQueue: List<String> = emptyList()
) : Parcelable