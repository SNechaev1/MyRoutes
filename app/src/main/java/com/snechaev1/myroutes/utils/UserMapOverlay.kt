package com.snechaev1.myroutes.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.snechaev1.myroutes.R
import timber.log.Timber

class UserMapOverlay(private val context: Context) : SensorEventListener {

    private var accelerometerData = FloatArray(3)
    private var magnetometerData = FloatArray(3)
    var azimuth: Float = 0.0f   //The direction the device is pointing. 0 is magnetic north, measured in radians, and range from -π (-3.141) to π
    private var circle: Circle? = null
    private var marker: Marker? = null

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
//        marker?.rotation = event.values[0]
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> accelerometerData = event.values.clone()
            Sensor.TYPE_MAGNETIC_FIELD -> magnetometerData = event.values.clone()
            else -> return
        }
        val rotationMatrix = FloatArray(9)
        val rotationOK = SensorManager.getRotationMatrix(rotationMatrix,
                null, accelerometerData, magnetometerData)
        val orientationValues = FloatArray(3)
        if (rotationOK) {
            SensorManager.getOrientation(rotationMatrix, orientationValues);
        }
        azimuth = orientationValues[0]
//        Timber.d("onSensorChanged: $azimuth")
        marker?.rotation = azimuth

    }

    fun onLocationUpdate(location: Location, map: GoogleMap) {
        val latLng = location.toLatLng()
        val accuracy = location.accuracy.toDouble()
        val icon = ContextCompat.getDrawable(context, R.drawable.user_marker_compass)?.toBitmap()!!
        circle = circle ?: map.addCircle(CircleOptions()
                .center(latLng)
                .radius(accuracy)
                .fillColor(ContextCompat.getColor(context, R.color.brand_prim_transparent_10))
                .strokeWidth(0f))
        marker = marker ?: map.addMarker(MarkerOptions()
                .position(latLng)
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                .rotation(location.bearing)
                .flat(true))

        circle?.apply {
            center = latLng
            radius = accuracy
            Timber.d("onLocationUpdate circle: center - $center , radius - $radius ")
        }
        marker?.apply {
            position = latLng
            rotation = location.bearing
            Timber.d("onLocationUpdate marker: position - $position , rotation - $rotation ")
        }

    }

    fun register(sensorManager: SensorManager) {
        // Get updates from the accelerometer and magnetometer at a constant rate.
        // To make batch operations more efficient and reduce power consumption,
        // provide support for delaying updates to the application.
        //
        // In this example, the sensor reporting delay is small enough such that
        // the application receives an update before the system checks the sensor
        // readings again.
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                    this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL,
                    SensorManager.SENSOR_DELAY_UI
            )
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            sensorManager.registerListener(
                    this,
                    magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL,
                    SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    fun clear() {
        circle?.remove()
        marker?.remove()
        circle = null
        marker = null
    }

}