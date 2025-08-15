package com.xeniac.chillclub.feature_music_player.domain.errors

import com.xeniac.chillclub.core.domain.errors.Error

sealed class AdjustVolumeError : Error() {
    data object SomethingWentWrong : AdjustVolumeError()
}