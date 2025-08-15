package com.xeniac.chillclub.feature_music_player.presensation.states

import android.graphics.Rect
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.core.presentation.common.utils.UiText
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumePercentage

data class MusicPlayerState(
    val shouldCreateYouTubePlayer: Boolean = false,
    val isRadioStationsLoading: Boolean = false,
    val radioStations: List<RadioStation> = emptyList(),
    val currentRadioStationId: Long? = null,
    val currentRadioStation: RadioStation? = null,
    val musicVolumePercentage: MusicVolumePercentage = 0f,
    val isVolumeSliderVisible: Boolean = false,
    val volumeSliderBounds: Rect? = null,
    val isPlayInBackgroundEnabled: Boolean? = null,
    val youtubePlayer: YouTubePlayer? = null,
    val isMusicCued: Boolean = false,
    val isMusicBuffering: Boolean = false,
    val isMusicPlaying: Boolean = false,
    val errorMessage: UiText? = null,
    val isPermissionDialogVisible: Boolean = false,
    val notificationPermissionCount: Int = 0,
    val permissionDialogQueue: List<String> = emptyList()
)