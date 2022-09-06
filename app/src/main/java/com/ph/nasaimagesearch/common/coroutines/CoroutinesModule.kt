package com.ph.nasaimagesearch.common.coroutines

import com.ph.nasaimagesearch.common.coroutines.dispatcher.DefaultDispatcherProvider
import com.ph.nasaimagesearch.common.coroutines.dispatcher.DispatcherProvider
import com.ph.nasaimagesearch.common.coroutines.scope.AppScope
import com.ph.nasaimagesearch.common.coroutines.scope.ApplicationCoroutineScope
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
interface CoroutinesModule {

    @get:Binds
    val DefaultDispatcherProvider.bindDispatcherProvider: DispatcherProvider

    @AppScope
    @Binds
    fun ApplicationCoroutineScope.bindCoroutineScope(): CoroutineScope
}