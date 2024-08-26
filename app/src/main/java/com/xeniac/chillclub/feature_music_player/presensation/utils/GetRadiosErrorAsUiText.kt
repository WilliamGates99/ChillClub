package com.xeniac.chillclub.feature_music_player.presensation.utils

import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.utils.UiText
import com.xeniac.chillclub.feature_music_player.domain.utils.GetRadiosError

fun GetRadiosError.asUiText(): UiText = when (this) {
    GetRadiosError.Network.Offline -> UiText.StringResource(R.string.error_network_connection_unavailable)
    GetRadiosError.Network.ConnectTimeoutException -> UiText.StringResource(R.string.error_network_failure)
    GetRadiosError.Network.HttpRequestTimeoutException -> UiText.StringResource(R.string.error_network_failure)
    GetRadiosError.Network.SocketTimeoutException -> UiText.StringResource(R.string.error_network_failure)
    GetRadiosError.Network.RedirectResponseException -> UiText.StringResource(R.string.error_network_failure)
    GetRadiosError.Network.ClientRequestException -> UiText.StringResource(R.string.error_network_failure)
    GetRadiosError.Network.ServerResponseException -> UiText.StringResource(R.string.error_network_failure)
    GetRadiosError.Network.SerializationException -> UiText.StringResource(R.string.error_network_serialization)
    GetRadiosError.Network.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)

    GetRadiosError.Local.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)
}