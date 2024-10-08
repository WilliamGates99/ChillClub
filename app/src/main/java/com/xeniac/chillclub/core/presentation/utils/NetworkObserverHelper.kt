package com.xeniac.chillclub.core.presentation.utils

import com.xeniac.chillclub.core.domain.repositories.ConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object NetworkObserverHelper {

    val networkStatus = MutableStateFlow<ConnectivityObserver.Status?>(null)

    fun observeNetworkConnection(connectivityObserver: ConnectivityObserver) {
        CoroutineScope(Dispatchers.IO).launch {
            connectivityObserver.observe().onEach { status ->
                networkStatus.update { status }
            }.launchIn(scope = this)
        }
    }
}