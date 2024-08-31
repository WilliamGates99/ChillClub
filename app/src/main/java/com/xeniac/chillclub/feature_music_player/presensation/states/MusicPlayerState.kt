package com.xeniac.chillclub.feature_music_player.presensation.states

import android.os.Parcelable
import com.xeniac.chillclub.core.domain.models.Radio
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolume
import kotlinx.parcelize.Parcelize

@Parcelize
data class MusicPlayerState(
    val radios: List<Radio> = emptyList(),
    val musicVolume: MusicVolume? = null,
    val isRadiosLoading: Boolean = false,
    val notificationPermissionCount: Int = 0,
    val permissionDialogQueue: List<String> = emptyList(),
    val isPermissionDialogVisible: Boolean = false
) : Parcelable