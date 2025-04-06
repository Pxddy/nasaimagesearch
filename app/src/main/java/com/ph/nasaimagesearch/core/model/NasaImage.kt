package com.ph.nasaimagesearch.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NasaImage(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("photographer") val photographer: String?,
    @SerialName("location") val location: String?,
    @SerialName("description") val description: String?,
    @SerialName("date_created") val dateCreated: String,
    @SerialName("image_url") val imageUrl: String,
)