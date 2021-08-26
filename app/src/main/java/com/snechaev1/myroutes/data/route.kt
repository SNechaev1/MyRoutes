package com.snechaev1.myroutes.data

import java.io.Serializable

//@Entity(tableName = "routes")
data class Route(
//    @PrimaryKey(autoGenerate = true) val id: Int,
    var _id: String = "",
//    @PrimaryKey() var _created: Long = 0,
    var description: String = "",
//    var status: String = "",
    var finished: Long = 0,
    var imageNames: MutableList<String> = mutableListOf(),
//    var location: LocationData = LocationData(),
//    var comment: String = "",
//    var date: String = ""
): Serializable {
//    fun createdDate(): String = DateUtils.routeDate.format(_created).toString()
}
