package com.xeniac.chillclub.feature_music_player.presensation

import android.graphics.Rect

sealed interface MusicPlayerAction {
    data object GetRadioStations : MusicPlayerAction

    data class SetVolumeSliderBounds(val bounds: Rect) : MusicPlayerAction
    data object ShowVolumeSlider : MusicPlayerAction
    data object HideVolumeSlider : MusicPlayerAction
    data object DecreaseMusicVolume : MusicPlayerAction
    data object IncreaseMusicVolume : MusicPlayerAction
    data object PlayMusic : MusicPlayerAction
    data object PauseMusic : MusicPlayerAction

    data class OnPermissionResult(
        val permission: String,
        val isGranted: Boolean
    ) : MusicPlayerAction

    data class DismissPermissionDialog(val permission: String) : MusicPlayerAction
}