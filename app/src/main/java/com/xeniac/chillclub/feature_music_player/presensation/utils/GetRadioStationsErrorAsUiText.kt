package com.xeniac.chillclub.feature_music_player.presensation.utils

import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.common.utils.UiText
import com.xeniac.chillclub.feature_music_player.domain.utils.GetRadioStationsError

fun GetRadioStationsError.asUiText(): UiText = when (this) {
    GetRadioStationsError.Network.Offline -> UiText.StringResource(R.string.error_network_connection_unavailable)
    GetRadioStationsError.Network.ConnectTimeoutException -> UiText.StringResource(R.string.error_network_failure)
    GetRadioStationsError.Network.HttpRequestTimeoutException -> UiText.StringResource(R.string.error_network_failure)
    GetRadioStationsError.Network.SocketTimeoutException -> UiText.StringResource(R.string.error_network_failure)
    GetRadioStationsError.Network.RedirectResponseException -> UiText.StringResource(R.string.error_network_failure)
    GetRadioStationsError.Network.ClientRequestException -> UiText.StringResource(R.string.error_network_failure)
    GetRadioStationsError.Network.ServerResponseException -> UiText.StringResource(R.string.error_network_failure)
    GetRadioStationsError.Network.SerializationException -> UiText.StringResource(R.string.error_network_serialization)
    GetRadioStationsError.Network.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)

    GetRadioStationsError.Local.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)
}