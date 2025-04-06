package com.ph.nasaimagesearch

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import dagger.Lazy
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class NasaImageSearchApplication : Application(), SingletonImageLoader.Factory {

    @Inject
    lateinit var imageLoaderFactory: Lazy<SingletonImageLoader.Factory>

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader =
        imageLoaderFactory.get().newImageLoader(context)
}