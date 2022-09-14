package com.ph.nasaimagesearch.common.coil

import android.content.Context
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.request.CachePolicy
import com.ph.nasaimagesearch.R
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Provider

class CoilImageLoaderFactory @Inject constructor(
    @ApplicationContext private val contextProvider: Provider<Context>,
    private val okHttpClientProvider: Provider<OkHttpClient>,
    private val coilRetryInterceptorProvider: Provider<CoilRetryInterceptor>
) : ImageLoaderFactory {

    private val context: Context
        get() = contextProvider.get()

    private val okHttpClient: OkHttpClient
        get() = okHttpClientProvider.get()

    private val coilRetryInterceptor
        get() = coilRetryInterceptorProvider.get()

    override fun newImageLoader(): ImageLoader {
        Timber.d("newImageLoader()")
        return ImageLoader.Builder(context)
            .okHttpClient { okHttpClient }
            .diskCache { createDiskCache() }
            .diskCachePolicy(CachePolicy.ENABLED)
            .error(R.drawable.ic_baseline_broken_image_24)
            .crossfade(500)
            .components {
                add(coilRetryInterceptor)
            }
            .build()
    }

    private fun createDiskCache(): DiskCache {
        Timber.d("createDiskCache")
        val cacheFile = File(context.cacheDir, "coil_image_cache")
        return DiskCache.Builder()
            .directory(cacheFile)
            .build()
    }
}