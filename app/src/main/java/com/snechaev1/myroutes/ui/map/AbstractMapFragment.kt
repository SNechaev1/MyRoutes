package com.snechaev1.myroutes.ui.map

import android.Manifest
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.snechaev1.myroutes.R
import com.snechaev1.myroutes.utils.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class AbstractMapFragment : Fragment() {

    companion object {
        const val DEFAULT_ZOOM = 16f
        const val MAP_UPDATE_INTERVAL = 50_000L
    }

    protected val viewModel: MainViewModel by viewModels()
    private lateinit var locationProvider: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private lateinit var sensorManager: SensorManager
    var userLocationLatLng: LatLng? = null
    private var areaPolygon: Polygon? = null
    var userOverlay: UserMapOverlay? = null
    var userPositionInitialized: Boolean = false
    var map: GoogleMap? = null
//    var markerHelper: MarkerHelper? = null

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

    fun drawAreas(coords: List<List<List<Double>>>) {
        context?.let {
            val areaFillColor = ContextCompat.getColor(it, R.color.brand_prim_transparent_10)
            if (coords.isNotEmpty()) {
                val geo = mutableListOf<LatLng>().apply {
                    coords[0].forEach { list ->
                        this.add(LatLng(list[1], list[0]))
                    }
                }
                areaPolygon?.remove()
                areaPolygon = map?.addPolygon(PolygonOptions()
                        .addAll(geo)
                        .fillColor(areaFillColor)
                )
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

    fun checkGPSEnabled() {
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
                    locationProvider.locationFlow().collectLatest { location ->
                        // .doOnDispose { userOverlay?.clear() }
                        Timber.d("checkGPSPermission location: $location ")
                        userLocationLatLng = location.toLatLng()
                        if (!userPositionInitialized) {
                            userPositionInitialized = true
                            map?.let { map ->
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location.toLatLng(),
                                    DEFAULT_ZOOM
                                ))
                                // without bounds can't get vehicles
//                                markerHelper?.setGeoBounds(map)
//                                viewModel.mapUpdates(markerHelper)
                            }
                        }
                        map?.let { onLocationUpdate(location, it) }
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

