package com.snechaev1.myroutes.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.snechaev1.myroutes.R
import com.snechaev1.myroutes.databinding.MapFrBinding
import com.snechaev1.myroutes.utils.RouteDrawHelper
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MapsFragment : AbstractMapFragment() {

    private lateinit var binding: MapFrBinding

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
        }

        viewModel.routeActive.asLiveData().observe(viewLifecycleOwner) { routeActive ->
            with(binding.btnStartRoute) {
                if (routeActive) {
                    text =  getString(R.string.finish_route)
                    viewModel.userLocationLatLng.value?.let {
                        viewModel.path.value.add(it)
                    }
                    map?.let { RouteDrawHelper.drawRoute(context, it, viewModel.path.value) }
                } else {
                    text =  getString(R.string.start_route)
                    if (viewModel.path.value.isNotEmpty()) {

                    }
                    viewModel.path.value = mutableListOf()
                    RouteDrawHelper.removeRoute()
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

