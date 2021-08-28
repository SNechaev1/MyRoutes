package com.snechaev1.myroutes.ui.route_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.snechaev1.myroutes.R
import com.snechaev1.myroutes.data.model.Route
import com.snechaev1.myroutes.databinding.RouteDetailFrBinding
import com.snechaev1.myroutes.ui.map.MapViewModel
import com.snechaev1.myroutes.utils.RouteDrawHelper
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RouteDetailFragment : Fragment() {

    private lateinit var binding: RouteDetailFrBinding
    private val viewModel: RouteDetailViewModel by viewModels()
    private val mapViewModel: MapViewModel by activityViewModels()
    private lateinit var map: GoogleMap

    val callback = OnMapReadyCallback { googleMap ->
        Timber.d("map: $googleMap ")
        with(googleMap.uiSettings) {
            isMapToolbarEnabled = false
            isCompassEnabled = false
            isMyLocationButtonEnabled = false
        }
        map = googleMap
        setMapLocation()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = RouteDetailFrBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        arguments?.let {
            val route = it.getSerializable("route") as Route
            binding.route = route
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun setMapLocation() {
        if (!::map.isInitialized) return
        with(map) {
            arguments?.let {
                val route = it.getSerializable("route") as Route
                Timber.d("route: $route")
                drawRoute(route.path.list)
                setOnMapClickListener {
                    findNavController().popBackStack(R.id.nav_map, false)
                }
            }
        }
    }

    private fun drawRoute(path: List<LatLng>) {
        Timber.d("updateTripInfo: $path")
        map.let {
            when {
                path.isNotEmpty() -> {
                    context?.let { context -> RouteDrawHelper.drawRoute(context, it, path) }
                }
                else -> Unit
            }
        }
    }

}