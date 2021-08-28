package com.snechaev1.myroutes.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.snechaev1.myroutes.data.model.Route
import com.snechaev1.myroutes.network.ApiService
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

const val NETWORK_PAGE_SIZE = 3
const val STARTING_PAGE_INDEX = 1

class RoutePagingSource(private val service: ApiService) : PagingSource<Int, Route>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Route> {
        val pageNumber: Int = params.key ?: STARTING_PAGE_INDEX
        return try {
            // Start refresh at page 1 if undefined.
            val response = service.getRoutes(pageNumber, params.loadSize)
            val routes = response.data?.routeRequests ?: emptyList()
            Timber.d("load: $routes")

            val nextKey = if (routes.isNotEmpty()) {
                // initial params.loadSize = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                pageNumber.plus((params.loadSize / NETWORK_PAGE_SIZE))
            } else {
                null
            }
            Timber.d("pageNumber: $pageNumber , params: $params")
            Timber.d("key: ${params.key} , nextKey: $nextKey , loadSize: ${params.loadSize} , params: $params")
            LoadResult.Page(
                    data = routes,
                    prevKey = null,
                    nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Route>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}


