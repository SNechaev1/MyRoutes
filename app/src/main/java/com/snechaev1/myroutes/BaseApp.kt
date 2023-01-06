package com.snechaev1.myroutes

import android.app.Application
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

@HiltAndroidApp
class BaseApp : Application() {

    companion object {
        private var appContext: BaseApp? = null
        fun appContext(): BaseApp? {
            return appContext
        }
//        fun showErrToast(exc: ApiException) {
//            appContext?.let { exc.defaultHandler(it) }
//        }
        private val coroutineExcHandler = CoroutineExceptionHandler { _, exception ->
            Timber.d("coroutineExcHandler: $exception ")
            // log to Crashlytics, logcat, etc.
        }
        val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + coroutineExcHandler)
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
//        val badgeCount = 6
//        ShortcutBadger.applyCount(this, badgeCount)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
//        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.d("fcm Push token: ${task.result}")
            } else {
                Timber.d("Fetching FCM registration token failed: ${task.exception}")
            }
        }
    }

//    private fun isNetworkConnected(): Boolean {
//        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork = connectivityManager.activeNetwork
//        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
//        return networkCapabilities != null &&
//                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//    }

}