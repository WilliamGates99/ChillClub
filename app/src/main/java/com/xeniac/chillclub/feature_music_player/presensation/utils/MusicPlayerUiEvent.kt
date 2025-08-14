package com.xeniac.chillclub.feature_music_player.presensation.utils

import com.xeniac.chillclub.core.presentation.common.utils.Event

sealed class MusicPlayerUiEvent : Event() {
    data object StartYouTubePlayerService : MusicPlayerUiEvent()
    data object StopYouTubePlayerService : MusicPlayerUiEvent()
}