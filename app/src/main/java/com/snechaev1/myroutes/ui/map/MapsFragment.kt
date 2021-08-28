package com.snechaev1.myroutes.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.snechaev1.myroutes.R
import com.snechaev1.myroutes.data.DataRepository
import com.snechaev1.myroutes.data.model.LatLngList
import com.snechaev1.myroutes.data.model.Route
import com.snechaev1.myroutes.databinding.MapFrBinding
import com.snechaev1.myroutes.utils.RouteDrawHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MapsFragment : AbstractMapFragment() {

    private lateinit var binding: MapFrBinding
    @Inject
    lateinit var dataRepository: DataRepository

    val callback = OnMapReadyCallback { googleMap ->
        Timber.d("map: $googleMap ")
        map = googleMap
//        showBottomSheetDialogFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = MapFrBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.btnStartRoute.setOnClickListener {
            viewModel.routeActive.value = viewModel.routeActive.value.not()

            if (viewModel.routeActive.value) {
//                binding.btnStartRoute.text =  getString(R.string.finish_route)
                viewModel.userLocationLatLng.value?.let {
                    viewModel.path.value.add(it)
                }
                viewModel.route.value = Route(created = System.currentTimeMillis())
                if (map != null && context != null)
                    RouteDrawHelper.drawRoute(requireContext(), map!!, viewModel.path.value)
            } else {
//                binding.btnStartRoute.text =  getString(R.string.start_route)
                if (viewModel.path.value.isNotEmpty()) {
                    lifecycleScope.launch {
                        viewModel.route.value?.let { route ->
                            dataRepository.saveRoute(route.apply {
                                description = getString(R.string.route)
                                finished = System.currentTimeMillis()
                                path = LatLngList(viewModel.path.value)
                            })
                        }
                    }
                    viewModel.route.value = null
                    viewModel.path.value = mutableListOf()
                }
                RouteDrawHelper.removeRoute()
            }

        }

        viewModel.routeActive.asLiveData().observe(viewLifecycleOwner) { routeActive ->
            with(binding.btnStartRoute) {
                text = if (routeActive) {
                    getString(R.string.finish_route)
                } else {
                    getString(R.string.start_route)
                }
            }
        }

    }

//    fun location() {
//        userLocation?.apply {
//            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(this, DEFAULT_ZOOM))
//        }
//    }

    private fun showBottomSheetDialogFragment() {
        val bottomSheetFragment = MapBottomSheet()
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

}

