package com.xeniac.chillclub.feature_music_player.presensation

sealed interface MusicPlayerAction {
    data object GetRadios : MusicPlayerAction

    data class OnPermissionResult(
        val permission: String,
        val isGranted: Boolean
    ) : MusicPlayerAction

    data class DismissPermissionDialog(val permission: String) : MusicPlayerAction
}