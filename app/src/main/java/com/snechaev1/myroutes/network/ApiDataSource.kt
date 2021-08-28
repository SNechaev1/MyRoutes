package com.snechaev1.myroutes.network

import android.content.Context
import com.snechaev1.myroutes.data.model.ApiResource
import com.snechaev1.myroutes.data.model.ApiResponse
import com.snechaev1.myroutes.data.model.RouteList
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class ApiDataSource @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val apiService: ApiService,
//    private val db: AppDatabase
) {

    suspend fun getRoute(id: String) = getResult { apiService.getRoute(id) }
    suspend fun getRoutes() = getResult { apiService.getRoutes() }
    suspend fun postRoutes(routeList: RouteList) = getResult { apiService.postRoutes(routeList) }

    suspend fun setNotification(enable: Boolean) = getResult { apiService.setNotification(enable) }


//    private fun setPushToken() {
//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Timber.d("Fetching FCM registration token failed: ${task.exception}")
//                return@OnCompleteListener
//            }
//        })
//    }

    private suspend fun <T> getResult(call: suspend () -> ApiResponse<T>): ApiResource<T> {
        return try {
            val response = call()
            Timber.d("getResult message: ${response.message}")
            Timber.d("getResult context: $appContext")
            ApiResource(ApiResource.Status.SUCCESS, response.data, response.message, null)
        } catch (exc: Exception) {
            val apiExc = ErrorInterpreter().interpret(appContext, exc)
            Timber.d("getResult exc: $exc")
            ApiResource(ApiResource.Status.ERROR, null, apiExc.apiMessage, apiExc)
        }
    }

}