package com.ph.nasaimagesearch.core.external

import androidx.core.net.toUri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class NasaImageSearchPagingSourceProvider @Inject constructor(
    private val nasaImageSearchApiProvider: Provider<NasaImageSearchApi>
) {
    fun get(query: String) = NasaImageSearchPagingSource(
        nasaImageSearchApi = nasaImageSearchApiProvider.get(),
        query = query
    )
}

class NasaImageSearchPagingSource(
    private val nasaImageSearchApi: NasaImageSearchApi,
    private val query: String
) : PagingSource<Int, NasaImageSearchCollectionItem>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, NasaImageSearchCollectionItem> = try {
        Timber.d("loading data for query=%s and key=%s", query, params.key)
        val page = params.key ?: 1
        val collection = nasaImageSearchApi.search(q = query, page = page).collection
        val prevKey = collection.findPage(rel = "prev")
        val nextKey = collection.findPage(rel = "next")

        LoadResult.Page(
            data = collection.items,
            prevKey = prevKey,
            nextKey = nextKey
        ).also { Timber.d("returning %s", it) }
    } catch (e: Exception) {
        Timber.w(e, "Failed to load data for %s and params=%s", query, params)
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<Int, NasaImageSearchCollectionItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)

        }
    }
}

private fun NasaImageSearchCollection.findPage(rel: String): Int? = try {
    Timber.v("find page for rel=%s in links=%s", rel, links)
    links
        ?.find { it.rel == rel }
        ?.href
        ?.toUri()
        ?.getQueryParameter("page")
        ?.toIntOrNull()
        .also { Timber.v("returning page=%s", it) }
} catch (e: Exception) {
    Timber.w(e, "Failed to get page from %s", this)
    null
}