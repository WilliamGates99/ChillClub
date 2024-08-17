package com.xeniac.chillclub.feature_music_player.presensation

sealed class MusicPlayerEvent {
    data object GetRadios : MusicPlayerEvent()

    data class OnPermissionResult(
        val permission: String,
        val isGranted: Boolean
    ) : MusicPlayerEvent()

    data class DismissPermissionDialog(val permission: String) : MusicPlayerEvent()
}