package com.xeniac.chillclub.feature_music_player.presensation.utils

import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.common.utils.UiText
import com.xeniac.chillclub.feature_music_player.domain.errors.GetRadioStationsError

fun GetRadioStationsError.asUiText(): UiText = when (this) {
    GetRadioStationsError.Network.Offline -> UiText.StringResource(R.string.error_network_connection_unavailable)
    GetRadioStationsError.Network.ConnectTimeoutException -> UiText.StringResource(R.string.error_network_failure)
    GetRadioStationsError.Network.HttpRequestTimeoutException -> UiText.StringResource(R.string.error_network_failure)
    GetRadioStationsError.Network.SocketTimeoutException -> UiText.StringResource(R.string.error_network_failure)
    GetRadioStationsError.Network.SerializationException -> UiText.StringResource(R.string.error_network_serialization)
    GetRadioStationsError.Network.JsonConvertException -> UiText.StringResource(R.string.error_network_json_conversion)
    GetRadioStationsError.Network.SSLHandshakeException -> UiText.StringResource(R.string.error_network_ssl_handshake)
    GetRadioStationsError.Network.CertPathValidatorException -> UiText.StringResource(R.string.error_network_cert_path_validator)

    GetRadioStationsError.Network.RedirectResponseException -> UiText.StringResource(R.string.error_network_failure)

    GetRadioStationsError.Network.TooManyRequests -> UiText.StringResource(R.string.error_network_too_many_requests)
    GetRadioStationsError.Network.ClientRequestException -> UiText.StringResource(R.string.error_network_failure)

    GetRadioStationsError.Network.ServerResponseException -> UiText.StringResource(R.string.error_network_failure)

    GetRadioStationsError.Network.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)

    GetRadioStationsError.Local.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)
}