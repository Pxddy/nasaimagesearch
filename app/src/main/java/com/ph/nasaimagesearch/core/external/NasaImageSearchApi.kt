package com.ph.nasaimagesearch.core.external

import retrofit2.http.GET
import retrofit2.http.Query

interface NasaImageSearchApi {

    @GET("search")
    suspend fun search(
        @Query("q") q: String,
        @Query("page") page: Int = 1,
        @Query("media_type") mediaType: String = "image"
    ): NasaImageSearchResponse
}

// https://images-api.nasa.gov/search?q=sun&page=2