package com.ph.nasaimagesearch.core.external

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NasaImageSearchExternalModule {

    @Singleton
    @Provides
    fun provideNasaImageSearchApi(
        client: OkHttpClient,
    ): NasaImageSearchApi = Retrofit.Builder()
        .baseUrl("https://images-api.nasa.gov/")
        .client(client)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create()
}