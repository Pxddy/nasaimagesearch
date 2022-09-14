package com.ph.nasaimagesearch.common.coil

import coil.intercept.Interceptor
import coil.request.ErrorResult
import coil.request.ImageResult
import coil.request.SuccessResult
import com.ph.nasaimagesearch.common.network.state.NetworkStateProvider
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

class CoilRetryInterceptor @Inject constructor(
    private val networkStateProvider: NetworkStateProvider
) : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = chain.request
        return when (val result = chain.proceed(request)) {
            is SuccessResult -> result
            is ErrorResult -> result.retry(chain)
        }
    }

    private suspend fun ErrorResult.retry(chain: Interceptor.Chain): ImageResult = try {
        Timber.d("Image request failed. Retrying..")
        checkInternetConnection()
        val request = chain.request
        chain.proceed(request)
    } catch (e: Exception) {
        Timber.w(e, "Retry failed")
        this
    }

    private suspend fun checkInternetConnection() = withTimeout(10.minutes) {
        if (networkStateProvider.networkState.first().isNotConnected) {
            Timber.d("Not connected to Internet. Waiting for reconnect")
            networkStateProvider.networkState.filter { it.isConnected }.first()
            Timber.d("Reconnected! Proceeding with retry")
        }
    }
}