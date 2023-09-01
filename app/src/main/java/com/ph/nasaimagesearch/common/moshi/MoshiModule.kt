package com.ph.nasaimagesearch.common.moshi

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MoshiModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Reusable
    @Provides
    fun moshiConverterFactory(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory
        .create(moshi)
}