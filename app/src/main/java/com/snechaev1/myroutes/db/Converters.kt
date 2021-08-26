package com.snechaev1.myroutes.db

import androidx.room.TypeConverter
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
    fun fromStringList(value: MutableList<String>): String {
        return value.toString()
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isNotEmpty()) {
            value.split(",")
        } else {
            mutableListOf()
        }
    }

}
