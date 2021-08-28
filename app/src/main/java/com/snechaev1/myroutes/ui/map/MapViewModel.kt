package com.snechaev1.myroutes.ui.map

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.snechaev1.myroutes.data.model.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val routeActive = MutableStateFlow(false)
    val path: MutableStateFlow<MutableList<LatLng>> = MutableStateFlow(mutableListOf())
    val route: MutableStateFlow<Route?> = MutableStateFlow(null)
    val userLocationLatLng: MutableStateFlow<LatLng?> = MutableStateFlow(null)

    suspend fun mapUpdates() {
        Timber.d("mapUpdates")
    }

}
