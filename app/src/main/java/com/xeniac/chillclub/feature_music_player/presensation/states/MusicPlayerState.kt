package com.xeniac.chillclub.feature_music_player.presensation.states

import android.os.Parcelable
import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolume
import kotlinx.parcelize.Parcelize

@Parcelize
data class MusicPlayerState(
    val currentRadioStations: RadioStation? = null,
    val radioStations: List<RadioStation> = emptyList(),
    val isRadioStationsLoading: Boolean = false,
    val musicVolume: MusicVolume? = null,
    val notificationPermissionCount: Int = 0,
    val permissionDialogQueue: List<String> = emptyList(),
    val isPermissionDialogVisible: Boolean = false
) : Parcelable