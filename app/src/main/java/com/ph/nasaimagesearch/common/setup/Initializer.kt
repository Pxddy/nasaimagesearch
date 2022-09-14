package com.ph.nasaimagesearch.common.setup

import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class Initializer @Inject constructor(
    private val initializablesProvider: Provider<Set<@JvmSuppressWildcards Initializable>>
) {

    fun setup() {
        val initializables = initializablesProvider.get()
        Timber.d("Setting up %d initializables", initializables.size)
        initializables.forEach { it.initialize() }
    }
}