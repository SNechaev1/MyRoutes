package com.snechaev1.myroutes.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.snechaev1.myroutes.data.model.LatLngList
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

//    @TypeConverter
//    fun fromGeoJsonPoint(value: GeoJsonPoint?): String {
//        return value?.coordinates.toString()
//    }
//
//    @TypeConverter
//    fun toGeoJsonPoint(value: String?): GeoJsonPoint? {
//        return value?.let {
//            val coords = listOf(it.split(",")[0].toDouble(), it.split(",")[1].toDouble())
//            GeoJsonPoint(coordinates = coords)
//        }
//    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.toString()
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isNotEmpty()) {
            value.split(",")
        } else {
            emptyList()
        }
    }

//    @TypeConverter
//    fun fromLatLngList(value: List<LatLng>): String {
//        return value.toString()
//    }
//
//    @TypeConverter
//    fun toLatLngList(value: String): List<LatLng> {
//        return if (value.isNotEmpty()) {
//            value.split(",")
//        } else {
//            emptyList()
//        }
//    }


    @TypeConverter
    fun fromLatLngList(value: LatLngList?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toLatLngList(value: String?): LatLngList? {
        return try {
            Gson().fromJson(value, LatLngList::class.java)
        } catch (e: Exception) {
            null
        }
    }

}
