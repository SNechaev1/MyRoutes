package com.snechaev1.myroutes.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.snechaev1.myroutes.MainActivity
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.math.BigDecimal
import java.math.RoundingMode

fun FragmentActivity.restartMainActivity() {
    val intent = Intent(this, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    startActivity(intent)
}

fun FragmentActivity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    } else {
        this.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
}

fun Location.toLatLng() = LatLng(latitude, longitude)

fun Double.stripTrailingZeros(): String {
    return if (this == 0.0) "0"
    else BigDecimal(this).setScale(2, RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()
}

//val handlerThread = HandlerThread("MyHandlerThread")
//handlerThread.start()
//handlerThread.quit()
//looper.quit()
//handlerThread.quit()
// Implementation of a cold flow backed by a Channel that sends Location updates
@SuppressLint("MissingPermission")
fun FusedLocationProviderClient.locationFlow(isBackground: Boolean = true, looper: Looper = Looper.getMainLooper()) = callbackFlow {
    val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            result ?: return
            try {
                trySend(result).isSuccess
            } catch(e: Exception) {}
        }
    }
    val locationRequest = LocationRequest.create().apply {
        interval = 20_000
        if (isBackground) maxWaitTime = 100_000
        fastestInterval = 10_000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    requestLocationUpdates(locationRequest, callback, looper)
            .addOnFailureListener { exc ->
                close(exc) // in case of exception, close the Flow
//                    activity?.let {
//                        Toast.makeText(it.applicationContext, it.getString(R.string.could_not_define_location), Toast.LENGTH_SHORT).show()
//                    }
            }
    // clean up when Flow collection ends
    awaitClose {
        removeLocationUpdates(callback)
    }
}

fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}
