package com.ph.nasaimagesearch.common.network

data class NetworkState(
    val isConnected: Boolean,
    val isMetered: Boolean
) {
    val isNotConnected: Boolean
        get() = !isConnected
}
