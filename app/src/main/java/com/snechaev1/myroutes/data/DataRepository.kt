package com.snechaev1.myroutes.data

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.snechaev1.myroutes.BaseApp.Companion.appScope
import com.snechaev1.myroutes.data.model.ApiResource
import com.snechaev1.myroutes.data.model.Route
import com.snechaev1.myroutes.data.model.RouteList
import com.snechaev1.myroutes.db.AppDatabase
import com.snechaev1.myroutes.db.RouteDao
import com.snechaev1.myroutes.network.ApiDataSource
import com.snechaev1.myroutes.network.ApiService
import com.snechaev1.myroutes.ui.di.DbInMemory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class DataRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val apiService: ApiService,
    @DbInMemory private val db: AppDatabase,
    private val apiDataSource: ApiDataSource,
    private val routeDao: RouteDao
) {

    fun getRouteList(): Flow<PagingData<Route>> {
//        val pagingSourceFactory = { routeDao.getAllRoutes() }
//        @OptIn(ExperimentalPagingApi::class)
//        return Pager(
//            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
//            remoteMediator = RepReqBikesRemoteMediator(apiService, db),
//            pagingSourceFactory = pagingSourceFactory
//        ).flow
        return Pager(
//                initialKey = 1,
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RoutePagingSource(apiService) }
        ).flow
    }

    suspend fun getRoutes(): ApiResource<List<Route>> {
        val response = apiDataSource.getRoutes()
        if (response.status == ApiResource.Status.SUCCESS) {
            Timber.d("login success")
        }
        return response
    }

    suspend fun postRoutes(routeList: RouteList) {
        appScope.launch {
            Timber.d("routeList in thread ${Thread.currentThread().name}")
            apiDataSource.postRoutes(routeList)
        }
    }

}


