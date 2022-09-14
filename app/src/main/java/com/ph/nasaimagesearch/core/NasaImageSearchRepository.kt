package com.ph.nasaimagesearch.core

import androidx.paging.*
import com.ph.nasaimagesearch.core.external.NasaImageSearchCollectionItem
import com.ph.nasaimagesearch.core.external.NasaImageSearchPagingSourceProvider
import com.ph.nasaimagesearch.core.model.NasaImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NasaImageSearchRepository @Inject constructor(
    private val nasaImageSearchPagingSourceProvider: NasaImageSearchPagingSourceProvider
) {

    fun getSearchImageStream(query: String): Flow<PagingData<NasaImage>> {
        Timber.d("getSearchImageStream(query=%s)", query)
        return when (query.isEmpty()) {
            true -> flowOf(PagingData.empty())
            false -> Pager(
                config = PagingConfig(pageSize = 20),
                pagingSourceFactory = { nasaImageSearchPagingSourceProvider.get(query = query) }
            ).flow.map { pagingData ->
                pagingData
                    .filter { it.data.isNotEmpty() && !it.imageLinks.isNullOrEmpty() }
                    .map { it.toNasaImage() }
            }
        }
    }
}

private fun NasaImageSearchCollectionItem.toNasaImage(): NasaImage {
    val actualData = data.first()
    val imageLink = imageLinks!!.first().href

    return NasaImage(
        id = actualData.id,
        title = actualData.title,
        photographer = actualData.photographer,
        location = actualData.location,
        description = actualData.description,
        dateCreated = actualData.dateCreated,
        imageUrl = imageLink
    )
}