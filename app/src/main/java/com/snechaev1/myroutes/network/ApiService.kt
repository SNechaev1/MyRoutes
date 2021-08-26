package com.snechaev1.myroutes.network

import com.snechaev1.myroutes.data.ApiResponse
import com.snechaev1.myroutes.data.Route
import com.snechaev1.myroutes.data.RouteList
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
    suspend fun getRoutes(@Query("page") page: Int, @Query("limit") limit: Int): ApiResponse<List<Route>>

}