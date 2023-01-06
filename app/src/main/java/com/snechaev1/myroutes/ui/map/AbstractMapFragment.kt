package com.snechaev1.myroutes.ui.map

import android.Manifest
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.snechaev1.myroutes.utils.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class AbstractMapFragment : Fragment() {

    companion object {
        const val DEFAULT_ZOOM = 16f
        const val MAP_UPDATE_INTERVAL = 50_000L
    }

    protected val viewModel: MapViewModel by viewModels()
    private lateinit var locationProvider: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private lateinit var sensorManager: SensorManager
    var userOverlay: UserMapOverlay? = null
    var userPositionInitialized: Boolean = false
    var map: GoogleMap? = null

    private val requestGPSPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        // after dialog with permission closed onResume is invoke
        // all logic in onResume, onPause, just restore sendLocationTimes hear
        Timber.d("GPS permission granted: $isGranted")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = activity?.getSystemService(SENSOR_SERVICE) as SensorManager
        context?.let {
            locationProvider = LocationServices.getFusedLocationProviderClient(it)
            userOverlay = it.let(::UserMapOverlay)
            locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the RESUMED state (or above).
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                checkGPSPermission()
            }
        }
    }

    open fun onLocationUpdate(location: Location, map: GoogleMap) {
        Timber.d("onLocationUpdate: $userOverlay")
        userOverlay?.onLocationUpdate(location, map)
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume: ")
        context?.apply {
            userOverlay?.register(sensorManager)
        }
    }

    private fun checkGPSEnabled() {
        context?.let {
            val gpsDisabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER).not()
            if (gpsDisabled) {
                DialogProvider.getNoGpsDialog(it) { dialog, _ -> dialog.cancel() }.create().show()
            }
        }
    }

    private suspend fun checkGPSPermission() {
        context?.let { context ->
            when {
                context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) -> { // The permission is granted
                    checkGPSEnabled()
                    Timber.d("get currentLocation ACCESS_FINE_LOCATION: true ")
                    locationProvider.locationFlow().collectLatest { locationResult ->
                        Timber.d("checkGPSPermission location: $locationResult ")
                        Timber.d("checkGPSPermission locations: ${locationResult.locations} ")
                        viewModel.userLocationLatLng.value = locationResult.lastLocation?.toLatLng()
                        if (viewModel.routeActive.value) {
                            viewModel.userLocationLatLng.value?.let { viewModel.path.value.add(it) }
                            map?.let { RouteDrawHelper.drawRoute(context, it, viewModel.path.value) }
                            Timber.d("userLocationLatLng: ${viewModel.userLocationLatLng.value} ")
                            Timber.d("path: ${viewModel.path.value} ")
                        }
                        if (!userPositionInitialized && locationResult.lastLocation != null) {
                            userPositionInitialized = true
                            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                locationResult.lastLocation!!.toLatLng(),
                                DEFAULT_ZOOM
                            ))
                        }
                        map?.let { locationResult.lastLocation?.let { it1 -> onLocationUpdate(it1, it) } }
                    }
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    // This case means user previously denied the permission
                    DialogProvider.gpsPermissionDialog(context) { dialog, _ ->
                        requestGPSPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        dialog.dismiss()
                    }.create().show()
                }
                else -> {
                    // Everything is fine you can simply request the permission
                    requestGPSPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause: ")
        userOverlay?.let { sensorManager.unregisterListener(it) }
    }

    fun zoom(map: GoogleMap, zoomType: ZoomType) {
        val position: CameraPosition? = map.cameraPosition
        position?.apply {
            val currentZoom = position.zoom
            val zoom: Float = when (zoomType) {
                ZoomType.ZoomIn -> currentZoom.plus(5)
                ZoomType.ZoomOut -> currentZoom.minus(1)
                else -> return
            }
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(position.target, zoom))
        }
    }

    enum class ZoomType {
        ZoomIn, ZoomOut, ZoomAbsolute
    }


}

