package com.ph.nasaimagesearch.core.external

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NasaImageSearchResponse(
    @SerialName("collection") val collection: NasaImageSearchCollection
)

@Serializable
data class NasaImageSearchCollection(
    @SerialName("items") val items: List<NasaImageSearchCollectionItem>,
    @SerialName("links") val links: List<NasaImageSearchLink>?
)

@Serializable
data class NasaImageSearchCollectionItem(
    @SerialName("data") val data: List<NasaImageSearchCollectionItemData>,
    @SerialName("links") val imageLinks: Set<NasaImageSearchLink>?
)

@Serializable
data class NasaImageSearchCollectionItemData(
    @SerialName("nasa_id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("photographer") val photographer: String?,
    @SerialName("location") val location: String?,
    @SerialName("description") val description: String?,
    @SerialName("date_created") val dateCreated: String
)

@Serializable
data class NasaImageSearchLink(
    @SerialName("href") val href: String,
    @SerialName("rel") val rel: String
)