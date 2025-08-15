package com.xeniac.chillclub.feature_music_player.domain.errors

import com.xeniac.chillclub.core.domain.errors.Error

sealed class StoreCurrentlyPlayingRadioStationIdError : Error() {
    data object SomethingWentWrong : StoreCurrentlyPlayingRadioStationIdError()
}