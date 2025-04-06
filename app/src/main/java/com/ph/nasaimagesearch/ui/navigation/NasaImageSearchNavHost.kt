package com.ph.nasaimagesearch.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ph.nasaimagesearch.ui.details.nasaImageSearchDetails
import com.ph.nasaimagesearch.ui.details.navigateToNasaImageSearchDetails
import com.ph.nasaimagesearch.ui.overview.NasaImageSearchOverviewRoute
import com.ph.nasaimagesearch.ui.overview.nasaImageSearchOverview

@Composable
fun NasaImageSearchNavHost(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
) {

    NavHost(
        navController = navHostController,
        startDestination = NasaImageSearchOverviewRoute,
        modifier = modifier,
    ) {
        nasaImageSearchOverview(onItemClick = { navHostController.navigateToNasaImageSearchDetails(image = it) })

        nasaImageSearchDetails(onBackClick = navHostController::navigateUp)
    }
}