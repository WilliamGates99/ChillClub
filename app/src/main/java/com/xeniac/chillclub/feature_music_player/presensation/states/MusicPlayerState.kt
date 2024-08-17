package com.xeniac.chillclub.feature_music_player.presensation.states

import android.os.Parcelable
import com.xeniac.chillclub.feature_music_player.domain.models.Radio
import kotlinx.parcelize.Parcelize

@Parcelize
data class MusicPlayerState(
    val radios: List<Radio> = emptyList(),
    val isRadiosLoading: Boolean = false,
    val notificationPermissionCount: Int = 0,
    val permissionDialogQueue: List<String> = emptyList(),
    val isPermissionDialogVisible: Boolean = false
) : Parcelable