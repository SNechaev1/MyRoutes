package com.snechaev1.myroutes.ui.route_detail

import androidx.lifecycle.ViewModel
import com.snechaev1.myroutes.data.DataRepository
import com.snechaev1.myroutes.data.model.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class RouteDetailViewModel @Inject constructor(
//    private val savedStateHandle: SavedStateHandle,
//    @ApplicationContext context: Context,
    private val dataRepository: DataRepository,
//    private val apiDataSource: ApiDataSource
): ViewModel() {

    val route: MutableStateFlow<Route?> = MutableStateFlow(null)

    fun deleteRoute(route: Route) {
        dataRepository.deleteRoute(route)
    }

}
