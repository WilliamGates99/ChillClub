package com.xeniac.chillclub.feature_music_player.domain.errors

import com.xeniac.chillclub.core.domain.errors.Error

sealed class GetRadioStationsError : Error() {
    sealed class Network : GetRadioStationsError() {
        data object Offline : Network()
        data object ConnectTimeoutException : Network()
        data object HttpRequestTimeoutException : Network()
        data object SocketTimeoutException : Network()
        data object SerializationException : Network()
        data object JsonConvertException : Network()
        data object SSLHandshakeException : Network()
        data object CertPathValidatorException : Network()

        // 3xx errors
        data object RedirectResponseException : Network()

        // 4xx errors
        data object TooManyRequests : Network()
        data object ClientRequestException : Network()

        // 5xx
        data object ServerResponseException : Network()

        data object SomethingWentWrong : Network()
    }

    sealed class Local : GetRadioStationsError() {
        data object SomethingWentWrong : GetRadioStationsError()
    }
}