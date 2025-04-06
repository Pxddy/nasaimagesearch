package com.ph.nasaimagesearch.ui.overview

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ph.nasaimagesearch.core.model.NasaImage
import kotlinx.serialization.Serializable

@Serializable
data object NasaImageSearchOverviewRoute

fun NavGraphBuilder.nasaImageSearchOverview(onItemClick: (NasaImage) -> Unit) {
    composable<NasaImageSearchOverviewRoute> {
        NasaImageSearchOverviewScreen(onItemClick = onItemClick)
    }
}