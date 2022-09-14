package com.ph.nasaimagesearch.common.network.state

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import timber.log.Timber

class NetworkChangedCallback : ConnectivityManager.NetworkCallback() {

    private val currentNetworkChanged = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val networkChanged: Flow<Unit> = currentNetworkChanged
        .debounce(300)


    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Timber.v("onAvailable(network=%s)", network)
        networkChanged()
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        Timber.v("onLost(network=%s)", network)
        networkChanged()
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
        super.onLosing(network, maxMsToLive)
        Timber.v("onLosing(network=%s, maxMsToLive=%s)", network, maxMsToLive)
        networkChanged()
    }

    override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
        super.onBlockedStatusChanged(network, blocked)
        Timber.v("onBlockedStatusChanged(network=%s, blocked=%s)", network, blocked)
        networkChanged()
    }

    override fun onCapabilitiesChanged(
        network: Network,
        networkCapabilities: NetworkCapabilities
    ) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        Timber.v(
            "onCapabilitiesChanged(network=%s, networkCapabilities=%s)",
            network,
            networkCapabilities
        )
        networkChanged()
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties)
        Timber.v("onLinkPropertiesChanged(network=%s, linkProperties=%s)", network, linkProperties)
        networkChanged()
    }

    private fun networkChanged() {
        currentNetworkChanged.tryEmit(Unit)
    }
}