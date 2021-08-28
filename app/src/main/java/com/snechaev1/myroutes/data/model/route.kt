package com.snechaev1.myroutes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.snechaev1.myroutes.utils.DateUtils
import java.io.Serializable

@Entity(tableName = "routes")
data class Route(
    var id: String = "",
    @PrimaryKey() var created: Long = 0,
    var description: String = "",
//    var status: String = "",
    var finished: Long = 0,
    var imageNames: MutableList<String> = mutableListOf(),
    var path: LatLngList = LatLngList(emptyList()),
//    var comment: String = "",
//    var date: String = ""
): Serializable {
    fun createdDate(): String = DateUtils.routeDate.format(created).toString()
}

data class RouteResponse(
//    var _id: String = "",
    var total: Int = 0,
    var routeRequests: List<Route> = emptyList()
)
data class RouteList(val list: List<Route>)
data class LatLngList(val list: List<LatLng>)


