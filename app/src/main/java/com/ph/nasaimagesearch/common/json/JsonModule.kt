package com.ph.nasaimagesearch.common.json

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
object JsonModule {

    @Provides
    fun json() = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }
}