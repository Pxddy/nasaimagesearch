package com.ph.nasaimagesearch.core.external

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NasaImageSearchResponse(
    @Json(name = "collection") val collection: NasaImageSearchCollection
)

@JsonClass(generateAdapter = true)
data class NasaImageSearchCollection(
    @Json(name = "items") val items: List<NasaImageSearchCollectionItem>,
    @Json(name = "links") val links: List<NasaImageSearchLink>?
)

@JsonClass(generateAdapter = true)
data class NasaImageSearchCollectionItem(
    @Json(name = "data") val data: List<NasaImageSearchCollectionItemData>,
    @Json(name = "links") val imageLinks: Set<NasaImageSearchLink>?
)

@JsonClass(generateAdapter = true)
data class NasaImageSearchCollectionItemData(
    @Json(name = "nasa_id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "photographer") val photographer: String?,
    @Json(name = "location") val location: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "date_created") val dateCreated: String
)

@JsonClass(generateAdapter = true)
data class NasaImageSearchLink(
    @Json(name = "href") val href: String,
    @Json(name = "rel") val rel: String
)