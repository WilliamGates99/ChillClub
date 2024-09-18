package com.xeniac.chillclub.feature_music_player.domain.utils

import com.xeniac.chillclub.core.domain.utils.Error

sealed class CurrentRadioStationError : Error() {
    data object SomethingWentWrong : CurrentRadioStationError()
}