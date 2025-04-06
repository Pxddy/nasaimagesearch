package com.ph.nasaimagesearch.common.coil

import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import coil3.Image
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.asImage
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import com.ph.nasaimagesearch.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
object CoilModule {

    @Provides
    fun singletonImageLoaderFactory(
        coilRetryInterceptor: CoilRetryInterceptor,
        okHttpClient: OkHttpClient,
    ) = SingletonImageLoader.Factory { context ->
        Timber.d("newImageLoader()")
        val client = okHttpClient.newBuilder().cache(null).build()

        ImageLoader.Builder(context)
            .crossfade(true)
            .components {
                add(OkHttpNetworkFetcherFactory(client))
                add(coilRetryInterceptor)
            }
            .error(context.getDrawableImageCompat(R.drawable.ic_baseline_broken_image_24))
            .build()
    }

    private fun PlatformContext.getDrawableImageCompat(@DrawableRes id: Int): Image? {
        val drawable = AppCompatResources.getDrawable(this, id)
        return drawable?.asImage()
    }
}