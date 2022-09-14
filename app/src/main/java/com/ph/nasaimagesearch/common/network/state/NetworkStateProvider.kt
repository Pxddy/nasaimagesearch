package com.ph.nasaimagesearch.common.network.state

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.net.ConnectivityManagerCompat
import com.ph.nasaimagesearch.common.coroutines.dispatcher.DispatcherProvider
import com.ph.nasaimagesearch.common.coroutines.flow.shareLatest
import com.ph.nasaimagesearch.common.coroutines.scope.AppScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkStateProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    @AppScope appScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider
) {

    // System services are cached in context
    private val connectivityManager: ConnectivityManager
        get() = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkState: Flow<NetworkState> = callbackFlow {
        suspend fun sendNetworkState() = send(createNetworkState())

        // Send initial state
        sendNetworkState()

        val callback = NetworkChangedCallback().also { networkChanged ->
            networkChanged.networkChanged
                .onEach { sendNetworkState() }
                .catch { Timber.e(it, "Network changed callback failed") }
                .launchIn(this)
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        try {
            connectivityManager.registerNetworkCallback(request, callback)
            Timber.d("Registered %s", callback)
        } catch (e: Exception) {
            Timber.e(e, "Failed to register network callback=%s", callback)
            val fallback = NetworkState(isConnected = true, isMetered = true)
            trySend(fallback)
        }

        awaitClose {
            Timber.d("Removing network callback %s", callback)
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.shareLatest(scope = appScope + dispatcherProvider.IO)

    private fun createNetworkState() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        networkState()
    } else {
        legacyNetworkState()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun networkState(): NetworkState {
        Timber.v("networkState()")
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = try {
            connectivityManager.getNetworkCapabilities(activeNetwork)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get network capabilities")
            null
        }

        return NetworkState(
            isConnected = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED),
            isMetered = !capabilities.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_NOT_METERED,
                default = true
            )
        ).also {
            Timber.v(
                "Created %s for network=%s with capabilities=%s",
                it,
                activeNetwork,
                capabilities
            )
        }
    }

    @Suppress("Deprecation")
    private fun legacyNetworkState(): NetworkState {
        Timber.v("legacyNetworkState()")
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        val isConnected = activeNetworkInfo?.isConnected ?: false
        val isMetered = ConnectivityManagerCompat.isActiveNetworkMetered(connectivityManager)

        return NetworkState(
            isConnected = isConnected,
            isMetered = isMetered
        ).also { Timber.v("Created %s for activeNetworkInfo=%s", it, activeNetworkInfo) }
    }
}

private fun NetworkCapabilities?.hasCapability(
    capability: Int,
    default: Boolean = false
): Boolean = this?.hasCapability(capability) ?: default