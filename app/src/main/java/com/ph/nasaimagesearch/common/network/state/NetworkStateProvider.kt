package com.ph.nasaimagesearch.common.network.state

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.ph.nasaimagesearch.common.coroutines.AppScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.plus
import kotlin.time.Duration

@Singleton
class NetworkStateProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    @AppScope appScope: CoroutineScope,
) {

    private val desiredNetworkCapability = NetworkCapabilities.NET_CAPABILITY_VALIDATED

    val isOnline: Flow<Boolean> = callbackFlow {
        val connectivityManager = checkNotNull(context.getSystemService(ConnectivityManager::class.java)) {
            "ConnectivityManager service missing"
        }
        val networks = MutableStateFlow(value = connectivityManager.filterActiveNetworksByCapability())

        networks
            .onEach { send(element = it) }
            .launchIn(scope = this)

        val callback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                Timber.v("onAvailable: $network")
                networks.update { it + network }
            }

            override fun onLost(network: Network) {
                Timber.v("onLost: $network")
                networks.update { it - network }
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(desiredNetworkCapability)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)
        Timber.v("NetworkCallback registered")

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
            Timber.v("NetworkCallback unregistered")
        }
    }
        .onEach { Timber.v("networks: $it") }
        .map { it.isNotEmpty() }
        .catch {
            Timber.e(it)
            emit(false)
        }
        .distinctUntilChanged()
        .onEach { Timber.v("isOnline: $it") }
        .shareIn(
            scope = appScope,
            started = SharingStarted.WhileSubscribed(replayExpiration = Duration.ZERO),
        )

    private fun ConnectivityManager.filterActiveNetworksByCapability(): Set<Network> = listOfNotNull(activeNetwork)
        .filter { getNetworkCapabilities(it)?.hasCapability(desiredNetworkCapability) == true }
        .toSet()
}