package com.ph.nasaimagesearch.core.external

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NasaImageSearchExternalModule {

    @Singleton
    @Provides
    fun provideNasaImageSearchApi(
        client: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): NasaImageSearchApi = Retrofit.Builder()
        .baseUrl("https://images-api.nasa.gov/")
        .client(client)
        .addConverterFactory(moshiConverterFactory)
        .build()
        .create()
}