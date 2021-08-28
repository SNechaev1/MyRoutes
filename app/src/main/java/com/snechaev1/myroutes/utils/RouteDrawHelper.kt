package com.snechaev1.myroutes.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.snechaev1.myroutes.R
import timber.log.Timber

object RouteDrawHelper {

    private var iconStart: BitmapDescriptor? = null
    private var iconFinish: BitmapDescriptor? = null
    private var startMarker: Marker? = null
    private var finishMarker: Marker? = null
    private var poly: Polyline? = null

    fun drawRoute(context: Context, map: GoogleMap, path: List<LatLng>) {
        iconStart = iconStart ?: BitmapHelper.vectorToBitmap(context, R.drawable.start)
        iconFinish = iconFinish ?: BitmapHelper.vectorToBitmap(context, R.drawable.finish)

        poly?.remove()
        Timber.d("drawTrip path: $path")
        poly = map.addPolyline(PolylineOptions()
                .color(ContextCompat.getColor(context, R.color.brand_prim))
                .width(20f)
                .addAll(path)
        )

        startMarker?.remove()
        finishMarker?.remove()
        startMarker = map.addMarker(path.first().let {
            MarkerOptions()
                    .icon(iconStart)
                    .position(it)
                    .anchor(0.5f, 0.5f)
        })
        finishMarker = map.addMarker(path.last().let {
            MarkerOptions()
                    .icon(iconFinish)
                    .position(it)
                    .anchor(0.5f, 0.5f)
        })
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(path.last(), 12f))
    }

    fun removeRoute() {
        poly?.remove()
        startMarker?.remove()
        finishMarker?.remove()
    }

}