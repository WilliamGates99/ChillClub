package com.xeniac.chillclub.feature_music_player.domain.utils

import com.xeniac.chillclub.core.domain.utils.Error

sealed class AdjustVolumeError : Error() {
    data object SomethingWentWrong : AdjustVolumeError()
}