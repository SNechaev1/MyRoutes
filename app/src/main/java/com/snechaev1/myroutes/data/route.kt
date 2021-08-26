package com.snechaev1.myroutes.data

import androidx.room.Entity
import androidx.room.PrimaryKey
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
//    var location: LatLng? = null,
//    var comment: String = "",
//    var date: String = ""
): Serializable {
    fun createdDate(): String = DateUtils.routeDate.format(created).toString()
}

data class RouteList(val list: List<Route>)

