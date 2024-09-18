package com.xeniac.chillclub.core.data.repositories

import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import com.xeniac.chillclub.core.domain.repositories.ConnectivityObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConnectivityObserverImpl @Inject constructor(
    private val connectivityManager: ConnectivityManager
) : ConnectivityObserver {

    override fun observe(): Flow<ConnectivityObserver.Status> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                launch { send(ConnectivityObserver.Status.AVAILABLE) }
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                launch { send(ConnectivityObserver.Status.LOSING) }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                launch { send(ConnectivityObserver.Status.LOST) }
            }

            override fun onUnavailable() {
                super.onUnavailable()
                launch { send(ConnectivityObserver.Status.UNAVAILABLE) }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(
                /* networkCallback = */ callback
            )
        } else {
            send(ConnectivityObserver.Status.AVAILABLE)
        }

        awaitClose {
            connectivityManager.unregisterNetworkCallback(
                /* networkCallback = */ callback
            )
        }
    }.distinctUntilChanged()
}