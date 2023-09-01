package com.ph.nasaimagesearch.ui.details

import androidx.core.text.parseAsHtml
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ph.nasaimagesearch.R
import com.ph.nasaimagesearch.common.coroutines.dispatcher.DispatcherProvider
import com.ph.nasaimagesearch.common.time.toLongDateFormat
import com.ph.nasaimagesearch.core.model.NasaImage
import com.ph.nasaimagesearch.ui.destinations.NasaImageSearchDetailsScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import okhttp3.internal.toImmutableList
import timber.log.Timber
import java.time.Instant
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class NasaImageSearchDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    val uiState: StateFlow<UiState> = flow {
        val image = NasaImageSearchDetailsScreenDestination.argsFrom(savedStateHandle).image
        emit(image.toUiState())
    }
        .catch { cause ->
            Timber.e(cause, "Failed create UiState")
            emit(UiState.Error)
        }
        .stateIn(
            scope = viewModelScope + dispatcherProvider.Default,
            started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
            initialValue = UiState.Loading
        )

    fun onNavigateBack() = viewModelScope.launch {
        Timber.d("onNavigateBack()")
        eventChannel.send(Event.NavigateBack)
    }

    sealed interface Event {
        data object NavigateBack : Event
    }

    sealed interface UiState {
        data object Loading : UiState
        data object Error : UiState
        data class Success(
            val title: String,
            val imageUrl: String,
            val data: List<Pair<Int, String>>
        ) : UiState
    }
}

private fun NasaImage.toUiState(): NasaImageSearchDetailsViewModel.UiState {
    val data = mutableListOf(R.string.image_details_screen_data_date to dateCreated.formatDate())

    photographer?.let { data += R.string.image_details_screen_data_photographed to it }

    location?.let { data += R.string.image_details_screen_data_location to it }

    // Remove html code
    description?.let {
        data += R.string.image_details_screen_data_description to it.parseAsHtml().toString()
    }

    return NasaImageSearchDetailsViewModel.UiState.Success(
        title = title,
        imageUrl = imageUrl,
        data = data.toImmutableList()
    )
}

private fun String.formatDate(): String = Instant.parse(this).toLongDateFormat()