package com.ph.nasaimagesearch.common.coil

import coil3.intercept.Interceptor
import coil3.request.ErrorResult
import coil3.request.ImageResult
import coil3.request.SuccessResult
import com.ph.nasaimagesearch.common.network.state.NetworkStateProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

class CoilRetryInterceptor @Inject constructor(
    private val networkStateProvider: NetworkStateProvider
) : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        return when (val result = chain.proceed()) {
            is SuccessResult -> result
            is ErrorResult -> result.retry(chain)
        }
    }

    private suspend fun ErrorResult.retry(chain: Interceptor.Chain): ImageResult = try {
        Timber.d("Image request failed. Retrying..")
        checkInternetConnection()
        chain.proceed()
    } catch (e: Exception) {
        Timber.w(e, "Retry failed")
        this
    }

    private suspend fun checkInternetConnection() = withTimeout(10.minutes) {
        if (!networkStateProvider.isOnline.first()) {
            Timber.d("Not connected to Internet. Waiting for reconnect")
            networkStateProvider.isOnline.first { it }
            Timber.d("Reconnected! Proceeding with retry")
        }
    }
}