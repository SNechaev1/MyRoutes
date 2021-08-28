package com.snechaev1.myroutes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.snechaev1.myroutes.utils.DateUtils
import com.snechaev1.myroutes.utils.stripTrailingZeros
import java.io.Serializable
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@Entity(tableName = "routes")
data class Route(
//    var id: String = "",
    @PrimaryKey() var created: Long = 0,
    var description: String = "Route",
//    var duration: Long = 0,
//    val distance: Double = 0.0,
//    var status: String = "",
    var finished: Long = 0,
//    var imageNames: MutableList<String> = mutableListOf(),
    var path: LatLngList = LatLngList(emptyList()),
//    var comment: String = "",
): Serializable {
    fun createdDate(): String = DateUtils.routeDate.format(created).toString()

    @ExperimentalTime
    fun durationMs() : Duration = (finished - created).toDuration(TimeUnit.MILLISECONDS)
    @ExperimentalTime
    fun duration() = "${durationMs().inWholeHours}:${durationMs().inWholeMinutes.rem(60)}:${durationMs().inWholeSeconds.rem(60).rem(60)} "

    fun distance(): Double = SphericalUtil.computeLength(path.list)
    fun distanceKm(): String = distance().div(1000).stripTrailingZeros()+" km"
}

data class RouteResponse(
//    var _id: String = "",
    var total: Int = 0,
    var routeRequests: List<Route> = emptyList()
)

data class RouteList(val list: List<Route>)
data class LatLngList(val list: List<LatLng>)


