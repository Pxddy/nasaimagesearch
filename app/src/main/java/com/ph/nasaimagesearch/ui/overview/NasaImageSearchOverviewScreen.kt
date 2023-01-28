package com.ph.nasaimagesearch.ui.overview

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ph.nasaimagesearch.R
import com.ph.nasaimagesearch.core.model.NasaImage
import com.ph.nasaimagesearch.ui.composables.ErrorView
import com.ph.nasaimagesearch.ui.composables.LoadingView
import com.ph.nasaimagesearch.ui.destinations.NasaImageSearchDetailsScreenDestination
import com.ph.nasaimagesearch.ui.theme.Typography
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun NasaImageSearchOverviewScreen(
    navigator: DestinationsNavigator,
    viewModel: NasaImageSearchOverviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { topBar() },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(
                    space = dimensionResource(id = R.dimen.tiny_spacing)
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchBar(onSearchQuery = viewModel::search, isConnected = uiState.isConnected)

                if (uiState.searchQuery.isNotEmpty()) ImageGrid(
                    imageList = viewModel.items,
                    navigator = navigator,
                    searchQuery = uiState.searchQuery
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun topBar() = CenterAlignedTopAppBar(
    title = { Text(text = stringResource(id = R.string.app_name)) }
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    onSearchQuery: (String) -> Unit,
    isConnected: Boolean
) {
    val query = rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val (placeholderRes, leadingIconVector) = when (isConnected) {
        true -> R.string.image_overview_screen_search_placeholder to Icons.Default.Search
        false -> R.string.image_overview_screen_search_placeholder_no_connection to Icons.Default.Warning
    }

    OutlinedTextField(
        value = query.value,
        onValueChange = { query.value = it },
        enabled = isConnected,
        leadingIcon = {
            Icon(imageVector = leadingIconVector, contentDescription = null)
        },
        placeholder = {
            Text(text = stringResource(id = placeholderRes))
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchQuery(query.value)
                focusManager.clearFocus()
            }
        ),
        singleLine = true,
        shape = CircleShape,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.small_spacing))
    )
}

@Composable
private fun ImageGrid(
    imageList: Flow<PagingData<NasaImage>>,
    navigator: DestinationsNavigator,
    searchQuery: String
) {
    val imageListItems = imageList.collectAsLazyPagingItems()

    val refresh = imageListItems.loadState.refresh
    val isEmpty = imageListItems.itemCount == 0

    when {
        refresh is LoadState.Loading -> LoadingView()
        refresh is LoadState.Error -> ErrorView(
            errorButtonText = stringResource(id = R.string.image_overview_screen_error_button_text)
        ) { imageListItems.retry() }
        refresh is LoadState.NotLoading && isEmpty -> EmptyListView(searchQuery)
        refresh is LoadState.NotLoading -> imageListItems.show(navigator)
    }
}

@Composable
private fun LazyPagingItems<NasaImage>.show(navigator: DestinationsNavigator) {
    val imageListItems = this
    val space = dimensionResource(id = R.dimen.tiny_spacing)

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = space),
        horizontalArrangement = Arrangement.spacedBy(space),
        verticalArrangement = Arrangement.spacedBy(space)
    ) {
        items(
            count = imageListItems.itemCount,
            key = { imageListItems[it]?.id ?: it }
        ) { index ->
            val item = imageListItems[index]
            ImageGridItem(item = item, navigator = navigator)
        }
    }
}

@Composable
private fun ImageGridItem(item: NasaImage?, navigator: DestinationsNavigator) {
    val modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1.778f)

    if (item != null) {
        AsyncImage(
            model = item.imageUrl,
            modifier = modifier.clickable {
                navigator.navigate(NasaImageSearchDetailsScreenDestination(image = item))
            },
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
    } else {
        CircularProgressIndicator(modifier = modifier)
    }
}

@Composable
private fun EmptyListView(searchQuery: String) {
    val configuration = LocalConfiguration.current
    val modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.small_spacing))

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> Row(
            modifier = modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            EmptyListViewContent(lottieAnimationWidth = 0.3f, searchQuery = searchQuery)
        }

        else -> Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmptyListViewContent(lottieAnimationWidth = 0.75f, searchQuery = searchQuery)
        }
    }
}

@Composable
private fun EmptyListViewContent(
    lottieAnimationWidth: Float,
    searchQuery: String
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_search_results))

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier.fillMaxWidth(lottieAnimationWidth)
    )

    if (composition == null) return

    Column(
        verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.tiny_spacing)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.image_overview_screen_search_empty_list_view_title),
            style = Typography.titleLarge
        )

        Text(
            text = stringResource(
                id = R.string.image_overview_screen_search_empty_list_view_body,
                searchQuery
            ),
            style = Typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}