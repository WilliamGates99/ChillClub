package com.xeniac.chillclub.feature_music_player.domain.utils

import com.xeniac.chillclub.core.domain.utils.Error

sealed class GetRadioStationsError : Error() {
    sealed class Network : GetRadioStationsError() {
        data object Offline : Network()
        data object ConnectTimeoutException : Network()
        data object HttpRequestTimeoutException : Network()
        data object SocketTimeoutException : Network()
        data object RedirectResponseException : Network()
        data object ClientRequestException : Network()
        data object ServerResponseException : Network()
        data object SerializationException : Network()

        data object SomethingWentWrong : Network()
    }

    sealed class Local : GetRadioStationsError() {
        data object SomethingWentWrong : GetRadioStationsError()
    }
}