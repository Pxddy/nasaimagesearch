package com.ph.nasaimagesearch.common.network

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val cacheDir = File(context.cacheDir, CACHE_DIR)
        val cache = Cache(cacheDir, DEFAULT_HTTP_CACHE_SIZE)

        return OkHttpClient.Builder()
            .cache(cache)
            .build()
    }
}

private const val DEFAULT_HTTP_CACHE_SIZE: Long = 50 * 1024 * 1024 //50MB
private const val CACHE_DIR = "http_cache"