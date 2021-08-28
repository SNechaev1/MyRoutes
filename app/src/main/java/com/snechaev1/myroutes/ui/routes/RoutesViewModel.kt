package com.snechaev1.myroutes.ui.routes

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.snechaev1.myroutes.data.model.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class RoutesViewModel @Inject constructor(
    @ApplicationContext appContext: Context,
//    private val dataRepository: DataRepository
): ViewModel() {

    val routes = MutableLiveData<PagingData<Route>>()

    // routes in active state
//    suspend fun getRoutes() {
//        dataRepository.getRoutes(status = ACTIVE)
//            .collectLatest { pagingData ->
//                Timber.d("pagingData: $pagingData")
//                routes.value = pagingData
//        }
//    }

}