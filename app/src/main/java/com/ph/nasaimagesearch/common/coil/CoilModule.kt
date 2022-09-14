package com.ph.nasaimagesearch.common.coil

import com.ph.nasaimagesearch.common.setup.Initializable
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface CoilModule {

    @Binds
    @IntoSet
    fun CoilInitializer.bind(): Initializable
}