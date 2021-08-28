package com.snechaev1.myroutes.network

import com.snechaev1.myroutes.data.model.ApiResponse
import com.snechaev1.myroutes.data.model.Route
import com.snechaev1.myroutes.data.model.RouteList
import com.snechaev1.myroutes.data.model.RouteResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("routes")
    suspend fun postRoutes(@Body routeList: RouteList): ApiResponse<String>

    @GET("route")
    suspend fun getRoute(@Query("id") id: String): ApiResponse<Route>

    @GET("routes")
    suspend fun getRoutes(): ApiResponse<List<Route>>

    @GET("routes")
    suspend fun getRoutes(@Query("page") page: Int, @Query("limit") limit: Int): ApiResponse<RouteResponse>

    @GET("setNotification")
    suspend fun setNotification(@Query("enable") enable: Boolean): ApiResponse<String>

}