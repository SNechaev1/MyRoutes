package com.snechaev1.myroutes.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.snechaev1.myroutes.R
import com.snechaev1.myroutes.databinding.MapFrBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import timber.log.Timber

@AndroidEntryPoint
class MapsFragment : AbstractMapFragment() {

    private lateinit var binding: MapFrBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    val callback = OnMapReadyCallback { googleMap ->
        Timber.d("map: $googleMap ")
        with(googleMap.uiSettings) {
            isMapToolbarEnabled = false
            isCompassEnabled = false
            isMyLocationButtonEnabled = true
        }
        map = googleMap
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
    }

//    fun location() {
//        userLocation?.apply {
//            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(this, DEFAULT_ZOOM))
//        }
//    }

    private fun showBottomSheetDialogFragment(vehicleQr: String) {
        val bottomSheetFragment = MapBottomSheet()
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

}

