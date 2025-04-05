package com.ph.nasaimagesearch.common.coroutines

import com.ph.nasaimagesearch.common.coroutines.dispatcher.DefaultDispatcherProvider
import com.ph.nasaimagesearch.common.coroutines.dispatcher.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CoroutinesModule {

    @Binds
    fun dispatcherProvider(defaultDispatcherProvider: DefaultDispatcherProvider): DispatcherProvider

    companion object {

        @Provides
        @Singleton
        @AppScope
        fun provideApplicationScope(dispatcherProvider: DispatcherProvider): CoroutineScope =
            CoroutineScope(SupervisorJob() + dispatcherProvider.Default)
    }
}

@Qualifier
@MustBeDocumented
@Retention
annotation class AppScope