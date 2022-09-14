package com.ph.nasaimagesearch.common.coil

import coil.Coil
import com.ph.nasaimagesearch.common.setup.Initializable
import timber.log.Timber
import javax.inject.Inject

class CoilInitializer @Inject constructor(
    private val coilImageLoaderFactory: CoilImageLoaderFactory
) : Initializable {
    override fun initialize() {
        Timber.d("initialize()")
        Coil.setImageLoader(coilImageLoaderFactory)
    }
}