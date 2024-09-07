package com.xeniac.chillclub.feature_music_player.presensation

sealed interface MusicPlayerAction {
    data object GetRadioStations : MusicPlayerAction

    data object PlayMusic : MusicPlayerAction
    data object PauseMusic : MusicPlayerAction
    data object IncreaseMusicVolume : MusicPlayerAction
    data object DecreaseMusicVolume : MusicPlayerAction

    data class OnPermissionResult(
        val permission: String,
        val isGranted: Boolean
    ) : MusicPlayerAction

    data class DismissPermissionDialog(val permission: String) : MusicPlayerAction
}