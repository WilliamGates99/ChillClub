package com.xeniac.chillclub.feature_music_player.presensation.utils

import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.common.utils.UiText
import com.xeniac.chillclub.feature_music_player.domain.errors.StoreCurrentlyPlayingRadioStationIdError

fun StoreCurrentlyPlayingRadioStationIdError.asUiText(): UiText = when (this) {
    StoreCurrentlyPlayingRadioStationIdError.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)
}