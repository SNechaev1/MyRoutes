package com.snechaev1.myroutes.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.snechaev1.myroutes.data.model.Route
import com.snechaev1.myroutes.db.AppDatabase
import com.snechaev1.myroutes.network.ApiService
import com.snechaev1.myroutes.ui.di.DbInMemory
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class RouteRemoteMediator(
    private val service: ApiService,
    @DbInMemory private val database: AppDatabase
    ) : RemoteMediator<Int, Route>() {

    private val routeDao = database.routeDao()
    var pageNumber = 2

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Route>
    ): MediatorResult {
        return try {
            // The network load method takes an optional after=<user.id>
            // parameter. For every page after the first, pass the last user
            // ID to let it continue from where it left off. For REFRESH,
            // pass null to load the first page.
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    Timber.d("load: LoadType.REFRESH");
                    null
                }
                // In this example, you never need to prepend, since REFRESH
                // will always load the first page in the list. Immediately
                // return, reporting end of pagination.
                LoadType.PREPEND -> {
                    Timber.d("load: LoadType.PREPEND");
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    Timber.d("load: LoadType.APPEND");
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )

//                    Timber.d("load lastIndex: ${state.pages.lastIndex}")
//                    Timber.d("load size: ${state.pages.size}");
//                    Timber.d("load anchorPosition: ${state.anchorPosition}")
                    // You must explicitly check if the last item is null when
                    // appending, since passing null to networkService is only
                    // valid for initial load. If lastItem is null it means no
                    // items were loaded after the initial REFRESH and there are
                    // no more items to load.

//                    lastItem._created
                    pageNumber
                }
            }

            // Suspending network load via Retrofit. This doesn't need to be
            // wrapped in a withContext(Dispatcher.IO) { ... } block since
            // Retrofit's Coroutine CallAdapter dispatches on a worker
            // thread.
//            val response = networkService.repairRequests(after = loadKey)
            // Start refresh at page 1 if undefined.
//            var repairRequests: MutableList<RepairRequest> = mutableListOf()
            var routeRequests: List<Route>

            if (loadKey == null) {
                val response = service.getRoutes(1, NETWORK_PAGE_SIZE)
                routeRequests = response.data?.routeRequests ?: emptyList()
                Timber.d("load: $routeRequests")
            } else {
                val response = service.getRoutes(loadKey.toInt(), NETWORK_PAGE_SIZE)
                routeRequests = response.data?.routeRequests ?: emptyList()
                Timber.d("load: $routeRequests")
                pageNumber += 1
            }

            database.withTransaction {
//                if (loadType == LoadType.REFRESH) {
//                    routeDao.deleteByQuery(query)
//                }

                // Insert new users into database, which invalidates the
                // current PagingData, allowing Paging to present the updates
                // in the DB.
                routeDao.insertAllRoutes(routeRequests)
            }

            MediatorResult.Success(
                endOfPaginationReached = routeRequests.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

}


