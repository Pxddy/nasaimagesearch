package com.ph.nasaimagesearch.ui.overview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ph.nasaimagesearch.common.coroutines.dispatcher.DispatcherProvider
import com.ph.nasaimagesearch.common.network.state.NetworkStateProvider
import com.ph.nasaimagesearch.core.NasaImageSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class NasaImageSearchOverviewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dispatcherProvider: DispatcherProvider,
    private val nasaImageSearchRepository: NasaImageSearchRepository,
    networkStateProvider: NetworkStateProvider
) : ViewModel() {

    private val queryFlow = savedStateHandle.getStateFlow(
        key = KEY_SEARCH_QUERY,
        initialValue = ""
    )

    val uiState = combine(queryFlow, networkStateProvider.networkState) { query, networkState ->
        UiState(
            searchQuery = query,
            isConnected = networkState.isConnected
        )
    }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
            initialValue = UiState()
        )

    val items = queryFlow
        .flatMapLatest { query -> nasaImageSearchRepository.getSearchImageStream(query = query) }
        .cachedIn(scope)

    fun search(query: String) {
        Timber.d("search(query=%s)", query)
        savedStateHandle[KEY_SEARCH_QUERY] = query
    }

    private val scope: CoroutineScope get() = viewModelScope + dispatcherProvider.Default

    data class UiState(
        val searchQuery: String = "",
        val isConnected: Boolean = false
    )
}

private const val KEY_SEARCH_QUERY = "key_search_query"