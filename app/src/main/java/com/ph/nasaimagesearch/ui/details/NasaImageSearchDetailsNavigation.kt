package com.ph.nasaimagesearch.ui.details

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import com.ph.nasaimagesearch.core.model.NasaImage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

@Serializable
data class NasaImageSearchDetailsRoute(@SerialName("image") val image: NasaImage)

fun NavController.navigateToNasaImageSearchDetails(image: NasaImage, navOptions: NavOptions? = null) {
    navigate(route = NasaImageSearchDetailsRoute(image = image), navOptions = navOptions)
}

fun NavGraphBuilder.nasaImageSearchDetails(onBackClick: () -> Unit) {
    composable<NasaImageSearchDetailsRoute>(typeMap = mapOf(typeOf<NasaImage>() to NasaImageType)) {
        NasaImageSearchDetailsScreen(onBackClick = onBackClick)
    }
}

val NasaImageType = object : NavType<NasaImage>(isNullableAllowed = false) {
    override fun put(bundle: Bundle, key: String, value: NasaImage) {
        bundle.putString(key, Json.encodeToString(value))
    }

    override fun get(bundle: Bundle, key: String): NasaImage? {
        val value = bundle.getString(key) ?: return null
        return Json.decodeFromString(value)
    }

    override fun serializeAsValue(value: NasaImage): String = Uri.encode(Json.encodeToString(value))

    override fun parseValue(value: String): NasaImage = Json.decodeFromString(value)
}