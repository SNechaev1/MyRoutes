package com.snechaev1.myroutes.ui.statistics

import androidx.lifecycle.ViewModel
import com.snechaev1.myroutes.data.DataRepository
import com.snechaev1.myroutes.data.model.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val dataRepository: DataRepository
): ViewModel() {

    val routes = MutableStateFlow<List<Route>>(emptyList())

    suspend fun getRoutes() {
        routes.value = dataRepository.routesDb()
    }

}
