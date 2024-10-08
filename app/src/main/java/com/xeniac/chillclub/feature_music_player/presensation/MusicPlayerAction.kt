package com.xeniac.chillclub.feature_music_player.presensation

import android.graphics.Rect
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumePercentage

sealed interface MusicPlayerAction {
    data object GetRadioStations : MusicPlayerAction
    data object GetCurrentRadioStation : MusicPlayerAction

    data class PlayMusic(val radiosStation: RadioStation?) : MusicPlayerAction
    data object ResumeMusic : MusicPlayerAction
    data object PauseMusic : MusicPlayerAction
    data object ShowVolumeSlider : MusicPlayerAction
    data object HideVolumeSlider : MusicPlayerAction
    data class SetVolumeSliderBounds(val bounds: Rect) : MusicPlayerAction
    data class AdjustMusicVolume(val newPercentage: MusicVolumePercentage) : MusicPlayerAction

    data class InitializeYouTubePlayer(val player: YouTubePlayer) : MusicPlayerAction
    data class ShowYouTubePlayerError(val error: PlayerConstants.PlayerError) : MusicPlayerAction
    data class YouTubePlayerStateChanged(val state: PlayerConstants.PlayerState) : MusicPlayerAction

    data class OnPermissionResult(
        val permission: String,
        val isGranted: Boolean
    ) : MusicPlayerAction

    data class DismissPermissionDialog(val permission: String) : MusicPlayerAction
}