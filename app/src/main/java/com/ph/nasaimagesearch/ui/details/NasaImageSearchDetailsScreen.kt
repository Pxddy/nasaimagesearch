package com.ph.nasaimagesearch.ui.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ph.nasaimagesearch.R
import com.ph.nasaimagesearch.ui.composables.ErrorButtonAction
import com.ph.nasaimagesearch.ui.composables.ErrorView
import com.ph.nasaimagesearch.ui.composables.LoadingView
import com.ph.nasaimagesearch.ui.theme.Typography

@Composable
fun NasaImageSearchDetailsScreen(
    onBackClick: () -> Unit,
    viewModel: NasaImageSearchDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { topAppBar(onNavigateBackClick = onBackClick) },
        content = {
            content(
                uiState = uiState,
                paddingValues = it,
                errorButtonAction = onBackClick
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun topAppBar(onNavigateBackClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = R.string.image_details_screen_topbar_title)) },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.image_details_screen_topbar_navigation_icon_contentDescription),
                modifier = Modifier.clickable(onClick = onNavigateBackClick)
            )
        }
    )
}

@Composable
private fun content(
    uiState: NasaImageSearchDetailsViewModel.UiState,
    paddingValues: PaddingValues,
    errorButtonAction: ErrorButtonAction
) {
    val modifier = Modifier
        .padding(paddingValues)
        .padding(horizontal = dimensionResource(id = R.dimen.small_spacing))
        .fillMaxSize()

    when (uiState) {
        NasaImageSearchDetailsViewModel.UiState.Error -> ErrorView(
            modifier = modifier,
            errorButtonText = stringResource(id = R.string.image_details_screen_error_button_text),
            errorButtonAction = errorButtonAction
        )

        NasaImageSearchDetailsViewModel.UiState.Loading -> LoadingView()
        is NasaImageSearchDetailsViewModel.UiState.Success -> uiState.showDetails(
            modifier = modifier
        )
    }
}

@Composable
private fun NasaImageSearchDetailsViewModel.UiState.Success.showDetails(modifier: Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.tiny_spacing))
    ) {
        Text(text = title, style = Typography.titleLarge)

        AsyncImage(model = imageUrl, contentDescription = null)

        data.forEach { item ->
            Text(
                text = "${stringResource(id = item.first)} ${item.second}",
                modifier = Modifier.fillMaxWidth(),
                style = Typography.bodyLarge
            )
        }
    }
}